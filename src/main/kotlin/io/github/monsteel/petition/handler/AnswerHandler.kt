package io.github.monsteel.petition.handler

import io.github.monsteel.petition.domain.dto.petition.answer.AnswerDto
import io.github.monsteel.petition.domain.model.DataResponse
import io.github.monsteel.petition.domain.model.Response
import io.github.monsteel.petition.domain.model.petition.answer.AnswerDetailInfo
import io.github.monsteel.petition.service.jwt.JwtServiceImpl
import io.github.monsteel.petition.service.petition.answer.AnswerServiceImpl
import io.github.monsteel.petition.util.enum.PermissionType
import io.github.monsteel.petition.util.extension.toServerResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import javax.validation.Validator

@Component
class AnswerHandler(
    private val answerService:AnswerServiceImpl,
    private val jwtService: JwtServiceImpl
) {
    fun getAnswer(request: ServerRequest): Mono<ServerResponse> =
        Mono.just(request.pathVariable("idx"))
            .flatMap { answerService.fetchAnswer(it.toLong()) }
            .flatMap { Mono.just(it.map { answer -> AnswerDetailInfo(answer.idx!!, answer.petitionIdx!!, answer.writerID!!, answer.createdAt, answer.content!!) }) }
            .flatMap { DataResponse(HttpStatus.OK, "조회 완료", it).toServerResponse() }
            .onErrorResume { it.toServerResponse() }

    fun addAnswer(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(AnswerDto::class.java)
            .flatMap { Mono.zip(Mono.just(it), jwtService.validateToken(request.headers().firstHeader("x-access-token"))) }
            .flatMap {
                if(it.t2.permissionType != PermissionType.EXECUTIVE){
                    return@flatMap Mono.error(HttpClientErrorException(HttpStatus.UNAUTHORIZED, "권한 없음"))
                }else {
                    return@flatMap Mono.just(it)
                }
            }
            .flatMap { answerService.writeAnswer(it.t1,it.t2.userID.toString()) }
            .flatMap { Response(HttpStatus.OK, "작성 완료").toServerResponse() }
            .onErrorResume { it.toServerResponse() }
}