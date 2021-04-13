package io.github.monsteel.petition.service.petition.answer

import io.github.monsteel.petition.domain.dto.petition.answer.AnswerDto
import io.github.monsteel.petition.domain.entity.petition.Answer

interface AnswerService {
    fun fetchAnswer(petitionIdx:Long): List<Answer>

    fun writeAnswer(answerDto: AnswerDto)
}