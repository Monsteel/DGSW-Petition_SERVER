package io.github.monsteel.petition.handler

import io.github.monsteel.petition.domain.dto.auth.UserLoginDto
import io.github.monsteel.petition.domain.dto.auth.UserRegisterDto
import io.github.monsteel.petition.domain.model.DataResponse
import io.github.monsteel.petition.domain.model.Response
import io.github.monsteel.petition.domain.model.auth.UserInquiry
import io.github.monsteel.petition.domain.model.auth.UserToken
import io.github.monsteel.petition.service.auth.AuthServiceImpl
import io.github.monsteel.petition.service.jwt.JwtServiceImpl
import io.github.monsteel.petition.util.enum.JwtType
import io.github.monsteel.petition.util.extension.toServerResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class AuthHandler(
    private val authService: AuthServiceImpl,
    private val jwtService: JwtServiceImpl
) {
    fun login(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(UserLoginDto::class.java)
            .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.BAD_REQUEST, "잘못된 요청")))
            .flatMap(authService::login)
            .flatMap { Mono.zip(Mono.just(jwtService.createToken(it.idx!!, JwtType.ACCESS)), Mono.just(jwtService.createToken(it.idx!!, JwtType.REFRESH))) }
            .flatMap { DataResponse(HttpStatus.OK, "로그인 성공", UserToken(it.t1,it.t2)).toServerResponse() }
            .onErrorResume { it.toServerResponse() }

    fun register(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(UserRegisterDto::class.java)
            .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.BAD_REQUEST, "잘못된 요청")))
            .flatMap(authService::register)
            .flatMap { Response(HttpStatus.OK, "회원가입 성공").toServerResponse() }
            .onErrorResume { it.toServerResponse() }

    fun checkRegisteredUser(request: ServerRequest): Mono<ServerResponse> =
        Mono.justOrEmpty(request.queryParam("userId"))
            .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.BAD_REQUEST, "잘못된 요청")))
            .flatMap { checkRegisteredUser(it) }
            .onErrorResume { it.toServerResponse() }

    private fun checkRegisteredUser(userID: String) =
        authService.checkRegisteredUser(userID)
            .flatMap { DataResponse(HttpStatus.OK, "조회 성공", UserInquiry(false)).toServerResponse() }
            .onErrorResume { DataResponse(HttpStatus.OK, "조회 성공", UserInquiry(true)).toServerResponse() }
}