package io.github.monsteel.petition.service.petition.answer

import io.github.monsteel.petition.domain.dto.petition.answer.AnswerDto
import io.github.monsteel.petition.domain.entity.petition.Answer
import reactor.core.publisher.Mono

interface AnswerService {
    fun fetchAnswer(petitionIdx:Long): Mono<List<Answer>>

    fun writeAnswer(answerDto: AnswerDto, userID:String): Mono<Unit>
}