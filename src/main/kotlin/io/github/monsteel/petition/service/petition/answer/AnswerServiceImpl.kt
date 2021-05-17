package io.github.monsteel.petition.service.petition.answer

import io.github.monsteel.petition.domain.dto.petition.answer.AnswerDto
import io.github.monsteel.petition.domain.entity.User
import io.github.monsteel.petition.domain.entity.petition.Answer
import io.github.monsteel.petition.domain.repository.petition.AnswerRepo
import io.github.monsteel.petition.domain.repository.petition.PetitionRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpServerErrorException
import reactor.core.publisher.Mono

@Service
class AnswerServiceImpl(
    private val answerRepo: AnswerRepo,
    private val petitionRepo: PetitionRepo
):AnswerService {

    override fun fetchAllAnswerCount(): Mono<Int> {
        return Mono.just(answerRepo.findAllAnswerCount())
    }

    override fun fetchAnswer(petitionIdx:Long): Mono<List<Answer>> {
        return Mono.just(answerRepo.findAllByPetitionIdx(petitionIdx))
    }

    override fun writeAnswer(answerDto: AnswerDto, user: User):Mono<Unit> {
        val petition = petitionRepo.findByIdx(answerDto.petitionIdx!!)

        return Mono.justOrEmpty(answerRepo.save(Answer(petition, user, answerDto.content)))
                .flatMap {
                    petition.isAnswer = true
                    Mono.justOrEmpty(petitionRepo.save(petition))
                }
            .switchIfEmpty(Mono.error(HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"등록 실패")))
            .flatMap { Mono.just(Unit) }
    }
}