package io.github.monsteel.petition.handler

import io.github.monsteel.petition.domain.model.DataResponse
import io.github.monsteel.petition.domain.model.user.UserDetailInfo
import io.github.monsteel.petition.service.jwt.JwtServiceImpl
import io.github.monsteel.petition.util.extension.toISOString
import io.github.monsteel.petition.util.extension.toServerResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class MyInfoHandler(
        private val jwtService: JwtServiceImpl
) {
    fun getMyInfo(request: ServerRequest): Mono<ServerResponse> =
            jwtService.validateToken(request.headers().firstHeader("x-access-token"))
                    .flatMap { Mono.just(UserDetailInfo(it!!.idx!!, it.userID!!,it.permissionType!!, it.createdAt!!.toISOString())) }
                    .flatMap { DataResponse(HttpStatus.OK, "내 정보 조회 성공", it).toServerResponse() }
                    .onErrorResume { it.toServerResponse() }
}