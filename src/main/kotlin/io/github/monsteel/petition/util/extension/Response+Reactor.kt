package io.github.monsteel.petition.util.extension

import io.github.monsteel.petition.domain.model.Response
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

fun Response.toServerResponse(): Mono<ServerResponse> =
    ServerResponse.status(status)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(this)
