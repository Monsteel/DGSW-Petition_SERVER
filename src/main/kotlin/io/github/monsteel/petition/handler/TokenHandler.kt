package io.github.monsteel.petition.handler

import io.github.monsteel.petition.domain.model.DataResponse
import io.github.monsteel.petition.service.jwt.JwtServiceImpl
import io.github.monsteel.petition.util.extension.toServerResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class TokenHandler(
    private val jwtService: JwtServiceImpl
) {
    fun refreshToken(request: ServerRequest): Mono<ServerResponse> =
            jwtService.refreshToken(request.headers().firstHeader("refreshToken"))
                    .flatMap { DataResponse(HttpStatus.OK, "토큰 재발급 성공", it).toServerResponse() }
                    .onErrorResume { it.toServerResponse() }
}