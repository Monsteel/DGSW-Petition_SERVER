package io.github.monsteel.petition.handler

import io.github.monsteel.petition.domain.dto.petition.bulletin.PetitionDto
import io.github.monsteel.petition.domain.entity.petition.Petition
import io.github.monsteel.petition.domain.model.DataResponse
import io.github.monsteel.petition.domain.model.Response
import io.github.monsteel.petition.domain.model.petition.bulletin.PetitionSimpleInfo
import io.github.monsteel.petition.service.jwt.JwtServiceImpl
import io.github.monsteel.petition.service.petition.agree.AgreeServiceImpl
import io.github.monsteel.petition.service.petition.answer.AnswerServiceImpl
import io.github.monsteel.petition.service.petition.bulletin.PetitionServiceImpl
import io.github.monsteel.petition.util.Constant
import io.github.monsteel.petition.util.enum.PermissionType
import io.github.monsteel.petition.util.enum.PetitionFetchType
import io.github.monsteel.petition.util.extension.toServerResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.Validator


@Component
class PetitionHandler(
    private val petitionService: PetitionServiceImpl,
    private val answerService: AnswerServiceImpl,
    private val agreeService: AgreeServiceImpl,
    private val jwtService: JwtServiceImpl
) {
    /**
     * 청원 조회 API
     */
    fun getPetitions(request: ServerRequest): Mono<ServerResponse> =
        petitionService.fetchPetitions(
            request.queryParam("page").orElse("0").toInt(),
            request.queryParam("size").orElse("30").toInt(),
            PetitionFetchType.values().first { it.value == request.queryParam("type").orElse("3").toInt() }
        ).flatMap { DataResponse(HttpStatus.OK, "조회 성공", convertToPetitionSimpleInfoList(it)).toServerResponse() }

    /**
     * 청원 검색 API
     */
    fun searchPetition(request: ServerRequest): Mono<ServerResponse> =
        Mono.justOrEmpty(request.queryParam("keyword"))
            .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.BAD_REQUEST,"잘못된 요청")))
            .flatMap { petitionService.searchPetition(
                request.queryParam("page").orElse("0").toInt(),
                request.queryParam("size").orElse("30").toInt(),
                request.queryParam("keyword").orElse("")
            )}.flatMap { DataResponse(HttpStatus.OK, "검색 성공", convertToPetitionSimpleInfoList(it)).toServerResponse() }

    /**
     * 청원 작성 API
     */
    fun writePetition(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(PetitionDto::class.java)
            .flatMap { Mono.zip(Mono.just(it), jwtService.validateToken(request.headers().firstHeader("x-access-token"))) }
            .flatMap { petitionService.writePetition(it.t1, it.t2.userID.toString()) }
            .flatMap { Response(HttpStatus.OK, "작성 완료").toServerResponse() }

    /**
     * 청원 수정 API
     */
    fun editPetition(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(PetitionDto::class.java)
            .flatMap { Mono.zip(Mono.just(it),
                Mono.just(request.pathVariable("idx").toLong()),
                jwtService.validateToken(request.headers().firstHeader("x-access-token")),
                agreeService.fetchAgreeCount(request.pathVariable("idx").toLong())) }
            .flatMap {
                if(it.t4 > Constant.DO_NOT_MODIFY_AGREE_COUNT && it.t3.permissionType == PermissionType.STUDENT)
                    return@flatMap Mono.error(HttpClientErrorException(HttpStatus.UNAUTHORIZED, "최소 동의인원을 초과하여 수정할 수 없음"))
                else return@flatMap Mono.just(it)
            }
            .flatMap { petitionService.editPetition(it.t2,it.t1,it.t3.userID.toString()) }
            .flatMap { Response(HttpStatus.OK, "수정 완료").toServerResponse() }

    /**
     * 청원 삭제 API
     */
    fun deletePetition(request: ServerRequest): Mono<ServerResponse> =
        Mono.just(request.pathVariable("idx").toLong())
            .flatMap { Mono.zip(Mono.just(it), jwtService.validateToken(request.headers().firstHeader("x-access-token")), agreeService.fetchAgreeCount(it)) }
            .flatMap {
                if(it.t3 > Constant.DO_NOT_MODIFY_AGREE_COUNT && it.t2.permissionType == PermissionType.STUDENT)
                    return@flatMap Mono.error(HttpClientErrorException(HttpStatus.UNAUTHORIZED, "최소 동의인원을 초과하여 삭제할 수 없음"))
                else return@flatMap Mono.just(it)
            }
            .flatMap { petitionService.deletePetition(it.t1,it.t2.userID.toString()) }
            .flatMap { Response(HttpStatus.OK, "수정 완료").toServerResponse() }



    private fun convertToPetitionSimpleInfoList(petitionList: List<Petition>): Mono<PetitionSimpleInfo> =
        Flux.fromIterable(petitionList).flatMap { petition ->
            Mono.zip(Mono.just(petition), agreeService.fetchAgreeCount(petition.idx!!), answerService.fetchAnswer(petition.idx!!))
                .flatMap { Mono.just(PetitionSimpleInfo(it.t1.idx, it.t1.expirationDate, it.t1.category!!, it.t1.title!!, it.t2, it.t3.isNotEmpty())) }
        }.next()
}