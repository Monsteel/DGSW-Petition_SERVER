package io.github.monsteel.petition.service.petition.agree

import io.github.monsteel.petition.domain.dto.petition.agree.AgreeDto
import io.github.monsteel.petition.domain.dto.petition.answer.AnswerDto
import io.github.monsteel.petition.domain.entity.User
import io.github.monsteel.petition.domain.entity.petition.Agree
import reactor.core.publisher.Mono

interface AgreeService {

    fun fetchAgreeCount(petitionIdx:Long): Mono<Int>

    fun fetchAllAgreeCount(): Mono<Int>

    fun fetchAgree(page:Int, size:Int, petitionIdx:Long): Mono<List<Agree>>

    fun writeAgree(agreeDto: AgreeDto, user: User): Mono<Unit>
}