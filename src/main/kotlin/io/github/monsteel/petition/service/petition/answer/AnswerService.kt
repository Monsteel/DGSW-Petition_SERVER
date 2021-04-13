package io.github.monsteel.petition.service.petition.answer

import io.github.monsteel.petition.domain.entity.petition.Answer

interface AnswerService {
    fun fetchAnswer(petitionIdx:Long): List<Answer>
}