package io.github.monsteel.petition.util.extension

import io.github.monsteel.petition.domain.model.Response
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono


fun Throwable.toServerResponse(): Mono<ServerResponse> {
    return when (this) {
        is HttpClientErrorException -> { Response(this.statusCode, this.statusText).toServerResponse() }
        is HttpServerErrorException -> {  Response(this.statusCode, this.statusText).toServerResponse() }
        else -> { Response(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류").toServerResponse() }
    }
}