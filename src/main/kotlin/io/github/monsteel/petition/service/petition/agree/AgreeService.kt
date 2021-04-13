package io.github.monsteel.petition.service.petition.agree

import io.github.monsteel.petition.domain.dto.petition.agree.AgreeDto
import io.github.monsteel.petition.domain.dto.petition.answer.AnswerDto
import io.github.monsteel.petition.domain.entity.petition.Agree

interface AgreeService {

    fun fetchAgreeCount(petitionIdx:Long): Int

    fun fetchAgree(page:Int, size:Int, petitionIdx:Long): List<Agree>

    fun writeAgree(agreeDto: AgreeDto, userID:String)
}