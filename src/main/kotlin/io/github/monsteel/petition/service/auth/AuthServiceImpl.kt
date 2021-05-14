package io.github.monsteel.petition.service.auth

import io.github.monsteel.petition.domain.dto.auth.UserLoginDto
import io.github.monsteel.petition.domain.dto.auth.UserRegisterDto
import io.github.monsteel.petition.domain.entity.User
import io.github.monsteel.petition.domain.repository.OAuthRepo
import io.github.monsteel.petition.domain.repository.UserRepo
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import io.github.monsteel.petition.util.enum.PermissionType
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.client.HttpServerErrorException
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty


@Service
class AuthServiceImpl(
    private val userRepo: UserRepo,
    private val oAuthRepo: OAuthRepo
) : AuthService {

    @Value("\${registerKeys.student}")
      private lateinit var studentRegisterKey: String

    @Value("\${registerKeys.executive}")
      private lateinit var executiveRegisterKey: String

    /**
     * 회원가입
     */
    override fun register(userRegisterDto: UserRegisterDto): Mono<Unit> =
        checkValidRequest(userRegisterDto.googleToken, userRegisterDto.userID)
            .flatMap { checkRegisteredUser(userRegisterDto.userID) }
            .flatMap { save(userRegisterDto) }

    /**
     * 로그인
     */
    override fun login(userLoginDto: UserLoginDto): Mono<User> =
        checkValidRequest(userLoginDto.googleToken, userLoginDto.userID)
            .flatMap { Mono.just(userRepo.findAllByUserID(it).first()) }
            .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.UNAUTHORIZED, "가입되지 않은 사용자")))

    /**
     * 유효한 요청(정상적인 요청)인지 확인
     */
    override fun checkValidRequest(googleToken:String?, userID:String?): Mono<String> =
        Mono.justOrEmpty(oAuthRepo.fetchUserID(googleToken.toString()))
            .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.UNAUTHORIZED, "비정상적인 요청")))
            .flatMap { if(it == userID.toString()) Mono.just(it) else Mono.error(HttpClientErrorException(HttpStatus.UNAUTHORIZED, "비정상적인 요청")) }
    /**
     * 이미 가입된 유저인지 확인
     */
    override fun checkRegisteredUser(userID: String?): Mono<Unit> =
        Mono.just(userRepo.findAllByUserID(userID.toString()))
            .flatMap {
                if(it.isEmpty()){  Mono.just(Unit) }
                else{  Mono.error(HttpClientErrorException(HttpStatus.BAD_REQUEST, "이미 가입 된 사용자")) }
            }

    /**
     * 회원 저장
     */
    private fun save(userRegisterDto: UserRegisterDto): Mono<Unit> {
        return when (userRegisterDto.permissionKey) {
            studentRegisterKey ->
                Mono.justOrEmpty(userRepo.save(User(userRegisterDto.userID, PermissionType.STUDENT)))
                    .switchIfEmpty(Mono.error(HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입 실패")))
                    .flatMap { Mono.just(Unit) }

            executiveRegisterKey ->
                Mono.justOrEmpty(userRepo.save(User(userRegisterDto.userID, PermissionType.EXECUTIVE)))
                    .switchIfEmpty(Mono.error(HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입 실패")))
                    .flatMap { Mono.just(Unit) }
            else -> throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "올바르지 않은 가입코드")
        }
    }
}