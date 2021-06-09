package io.github.monsteel.petition.util.filter

import io.github.monsteel.petition.service.jwt.JwtServiceImpl
import io.github.monsteel.petition.util.extension.toServerResponse
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.HandlerFilterFunction
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class JwtFilter(
    private val jwtService: JwtServiceImpl
): HandlerFilterFunction<ServerResponse?, ServerResponse?> {

    override fun filter(serverRequest: ServerRequest, handlerFunction: HandlerFunction<ServerResponse?>): Mono<ServerResponse?> =
        jwtService.validateToken(serverRequest.headers().firstHeader("x-access-token"))
            .flatMap {
                serverRequest.attributes()["user"] = it
                handlerFunction.handle(serverRequest)
            }.onErrorResume {
                it.toServerResponse()
            }
}