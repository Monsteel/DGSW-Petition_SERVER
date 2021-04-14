package io.github.monsteel.petition.service.petition.answer

import io.github.monsteel.petition.domain.dto.petition.answer.AnswerDto
import io.github.monsteel.petition.domain.entity.petition.Answer
import io.github.monsteel.petition.domain.repository.petition.AnswerRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpServerErrorException
import reactor.core.publisher.Mono

@Service
class AnswerServiceImpl(
    private val answerRepo: AnswerRepo
):AnswerService {
    override fun fetchAnswer(petitionIdx:Long): Mono<List<Answer>> {
        return Mono.just(answerRepo.findAllByPetitionIdx(petitionIdx))
    }

    override fun writeAnswer(answerDto: AnswerDto, userID:String):Mono<Unit> {
        val answer = Answer()

        answer.writerID = userID
        answer.petitionIdx = answerDto.petitionIdx
        answer.content = answerDto.content

        return Mono.justOrEmpty(answerRepo.save(answer))
            .switchIfEmpty(Mono.error(HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"등록 실패")))
            .flatMap { Mono.just(Unit) }
    }
}