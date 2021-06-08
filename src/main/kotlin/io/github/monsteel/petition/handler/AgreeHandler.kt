package io.github.monsteel.petition.handler

import io.github.monsteel.petition.domain.dto.petition.agree.AgreeDto
import io.github.monsteel.petition.domain.model.DataResponse
import io.github.monsteel.petition.domain.model.Response
import io.github.monsteel.petition.domain.model.petition.agree.AgreeDetailInfo
import io.github.monsteel.petition.domain.model.petition.answer.AnswerDetailInfo
import io.github.monsteel.petition.service.jwt.JwtServiceImpl
import io.github.monsteel.petition.service.petition.agree.AgreeServiceImpl
import io.github.monsteel.petition.service.petition.bulletin.PetitionService
import io.github.monsteel.petition.util.Constant
import io.github.monsteel.petition.util.enum.PermissionType
import io.github.monsteel.petition.util.extension.isValidPetiton
import io.github.monsteel.petition.util.extension.toServerResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class AgreeHandler(
    private var agreeService: AgreeServiceImpl,
    private var petitionService: PetitionService,
    private val jwtService: JwtServiceImpl
) {
    fun getAgree(request: ServerRequest): Mono<ServerResponse> =
        Mono.justOrEmpty(request.queryParam("petitionIdx"))
            .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.BAD_REQUEST,"잘못된 요청")))
            .flatMap { agreeService.fetchAgree(
                request.queryParam("page").orElse("0").toInt(),
                request.queryParam("size").orElse("30").toInt(),
                request.queryParam("petitionIdx").orElse("").toLong()
            )}
            .flatMap { Mono.just(it.map { agree -> AgreeDetailInfo(agree.idx!!, agree.petition!!.idx!!, agree.user!!.userID!!, agree.createdAt!!, agree.content!!) }) }
            .flatMap { DataResponse(HttpStatus.OK, "조회 성공", it).toServerResponse() }
            .onErrorResume { it.toServerResponse() }

    fun agree(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(AgreeDto::class.java)
                .flatMap {  Mono.zip(petitionService.fetchPetitionDetailInfo(it.petitionIdx!!.toLong()), Mono.just(it)) }
                .flatMap {
                    if(it.t1.expirationDate!!.isValidPetiton()){
                        return@flatMap Mono.just(it)
                    } else {
                        return@flatMap Mono.error(HttpClientErrorException(HttpStatus.BAD_REQUEST, "종료된 청원에는 동의할 수 없음"))
                    }
                }
                .flatMap {
                    if(it.t1.isAnswer!!){
                        return@flatMap Mono.error(HttpClientErrorException(HttpStatus.BAD_REQUEST, "이미 답변된 청원에는 동의할 수 없음"))
                    } else {
                        return@flatMap Mono.just(it)
                    }
                }
            .flatMap { Mono.zip(Mono.just(it.t2), jwtService.validateToken(request.headers().firstHeader("x-access-token"))) }
            .flatMap { agreeService.writeAgree(it.t1,it.t2) }
            .flatMap { Response(HttpStatus.OK, "동의 완료").toServerResponse() }
            .onErrorResume { it.toServerResponse() }
}