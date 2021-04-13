package io.github.monsteel.petition.service.auth

import io.github.monsteel.petition.domain.dto.auth.UserLoginDto
import io.github.monsteel.petition.domain.dto.auth.UserRegisterDto
import io.github.monsteel.petition.domain.entity.User
import io.github.monsteel.petition.domain.repository.OAuthRepo
import io.github.monsteel.petition.domain.repository.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import io.github.monsteel.petition.util.enum.PermissionKey
import io.github.monsteel.petition.util.enum.PermissionType

@Service
class AuthServiceImpl : AuthService {
    @Autowired
    private lateinit var userRepo: UserRepo

    @Autowired
    private lateinit var oAuthRepo: OAuthRepo

    /**
     * 회원가입
     */
    override fun register(userRegisterDto: UserRegisterDto) {
        checkValidRequest(userRegisterDto.googleToken, userRegisterDto.userID)
        checkRegisteredUser(userRegisterDto.userID)

        when (userRegisterDto.permissionKey) {
            PermissionKey.EXECUTIVE.value -> {
                userRepo.save(User(userRegisterDto.userID, PermissionType.EXECUTIVE))
            }
            PermissionKey.STUDENT.value -> {
                userRepo.save(User(userRegisterDto.userID, PermissionType.STUDENT))
            }
            else -> {
                throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "올바르지 않은 요청")
            }
        }
    }

    /**
     * 로그인
     */
    override fun login(userLoginDto: UserLoginDto): Long? {
        checkValidRequest(userLoginDto.googleToken, userLoginDto.userID)
        return userRepo.findByUserID(userLoginDto.userID ?: "")?.idx
    }

    /**
     * 유효한 요청(정상적인 요청)인지 확인
     */
    override fun checkValidRequest(googleToken: String?, userID: String?) {
        if(googleToken.isNullOrBlank() || userID.isNullOrBlank()) {
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "검증 오류")
        }

        val userIdByToken = oAuthRepo.fetchUserID(googleToken)

        if(userIdByToken != userID) {
            throw HttpClientErrorException(HttpStatus.UNAUTHORIZED, "비정상적인 요청")
        }
    }

    /**
     * 이미 가입된 유저인지 확인
     */
    override fun checkRegisteredUser(userID: String?) {
        val isRegistered = (userRepo.findByUserID(userID ?: "") != null)

        if(isRegistered) {
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "이미 가입 된 사용자")
        }
    }

}