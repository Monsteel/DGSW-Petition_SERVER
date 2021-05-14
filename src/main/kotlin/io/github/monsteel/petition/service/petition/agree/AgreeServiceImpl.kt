package io.github.monsteel.petition.service.petition.agree

import io.github.monsteel.petition.domain.dto.petition.agree.AgreeDto
import io.github.monsteel.petition.domain.dto.petition.answer.AnswerDto
import io.github.monsteel.petition.domain.entity.User
import io.github.monsteel.petition.domain.entity.petition.Agree
import io.github.monsteel.petition.domain.entity.petition.Answer
import io.github.monsteel.petition.domain.repository.petition.AgreeRepo
import io.github.monsteel.petition.domain.repository.petition.PetitionRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import reactor.core.publisher.Mono

@Service
class AgreeServiceImpl(
    private val agreeRepo: AgreeRepo,
    private val petitionRepo: PetitionRepo
):AgreeService {
    override fun fetchAgreeCount(petitionIdx:Long): Mono<Int> {
        return Mono.just(agreeRepo.findAllByPetitionIdx(petitionIdx).count())
    }

    override fun fetchAgree(page:Int, size:Int, petitionIdx:Long): Mono<List<Agree>> {
        val pageable = PageRequest.of(page, size, Sort.by("idx").descending())
        return Mono.just(agreeRepo.findAllByPetitionIdx(petitionIdx, pageable))
    }

    override fun writeAgree(agreeDto: AgreeDto, user: User): Mono<Unit> {
        val isAgree = agreeRepo.findAllByPetitionIdxAndUserUserID(agreeDto.petitionIdx!!, user.userID!!).isNotEmpty()

        if(isAgree){
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "이미 동의한 청원")
        }

        return Mono.justOrEmpty(agreeRepo.save(Agree(agreeDto.petitionIdx, user, agreeDto.content)))
            .switchIfEmpty(Mono.error(HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"등록 실패")))
            .flatMap{ Mono.just(Unit) }
    }
}