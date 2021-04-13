package io.github.monsteel.petition.service.petition.answer

import io.github.monsteel.petition.domain.dto.petition.answer.AnswerDto
import io.github.monsteel.petition.domain.entity.petition.Answer
import io.github.monsteel.petition.domain.repository.petition.AnswerRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AnswerServiceImpl:AnswerService {
    @Autowired
    private lateinit var answerRepo: AnswerRepo

    override fun fetchAnswer(petitionIdx:Long): List<Answer> {
        return answerRepo.findAllByPetitionIdx(petitionIdx)
    }

    override fun writeAnswer(answerDto: AnswerDto) {
        val answer = Answer()

        answer.petitionIdx = answerDto.petitionIdx
        answer.content = answerDto.content

        answerRepo.save(answer)
    }
}