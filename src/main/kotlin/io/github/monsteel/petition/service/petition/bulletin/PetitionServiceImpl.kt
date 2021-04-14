package io.github.monsteel.petition.service.petition.bulletin

import io.github.monsteel.petition.domain.dto.petition.bulletin.PetitionDto
import io.github.monsteel.petition.domain.entity.petition.Petition
import io.github.monsteel.petition.domain.repository.petition.AnswerRepo
import io.github.monsteel.petition.domain.repository.petition.PetitionRepo
import io.github.monsteel.petition.service.jwt.JwtServiceImpl
import io.github.monsteel.petition.util.enum.PetitionFetchType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerErrorException
import reactor.core.publisher.Mono

@Service
class PetitionServiceImpl(
    private val petitionRepo: PetitionRepo,
    private val answerRepo: AnswerRepo
):PetitionService {
    override fun fetchPetitions(page:Int, size:Int, type:PetitionFetchType): Mono<List<Petition>> {
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())

        return when(type) {
            PetitionFetchType.ON_GOING ->
                Mono.just(petitionRepo.findAllByExpirationDateAfter(pageable))

            PetitionFetchType.TERMINATED ->
                Mono.just(petitionRepo.findAllByExpirationDateBefore(pageable))

            PetitionFetchType.FINISHED ->
                Mono.just(answerRepo.findAll().map { petitionRepo.findByIdx(it.petitionIdx!!) })

            PetitionFetchType.ALL ->
                Mono.just(petitionRepo.findAll(pageable).toList())
        }
    }

    override fun searchPetition(page:Int, size:Int, keyword:String): Mono<List<Petition>> {
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        return Mono.just(petitionRepo.findAllByTitleContainsOrFirstKeywordContainsOrSecondKeywordContainsOrThirdKeywordContains(keyword,keyword,keyword,keyword,pageable))
    }

    override fun fetchTopTenPetition(page:Int, size:Int): List<Petition> {
        return petitionRepo.findAll(PageRequest.of(0, 10, Sort.by("동의 수").descending())).toList()
    }

    override fun writePetition(petitionDto: PetitionDto, userID:String): Mono<Unit> {
        val petition = Petition()

        petition.init(
            userID,
            petitionDto.category,
            petitionDto.title,
            petitionDto.content,
            petitionDto.firstKeyword,
            petitionDto.secondKeyword,
            petitionDto.thirdKeyword
        )

        return Mono.justOrEmpty(petitionRepo.save(petition))
            .switchIfEmpty(Mono.error(HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "작성 실패")))
            .flatMap { Mono.just(Unit) }
    }

    override fun editPetition(idx:Long, petitionDto: PetitionDto, userID:String): Mono<Unit> {
        val updatePetition = petitionRepo.findByIdx(idx)

        if(updatePetition.writerID != userID) {
            throw HttpClientErrorException(HttpStatus.UNAUTHORIZED, "권한 없음")
        }

        updatePetition.mod(
            petitionDto.category,
            petitionDto.title,
            petitionDto.content,
            petitionDto.firstKeyword,
            petitionDto.secondKeyword,
            petitionDto.thirdKeyword
        )

        return Mono.justOrEmpty(petitionRepo.save(updatePetition))
            .switchIfEmpty(Mono.error(HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "수정 실패")))
            .flatMap { Mono.just(Unit) }
    }

    override fun deletePetition(idx:Long, userID:String):Mono<Unit> {
        val deletePetition = petitionRepo.findByIdx(idx)

        if(deletePetition.writerID != userID) {
            throw HttpClientErrorException(HttpStatus.UNAUTHORIZED, "권한 없음")
        }

        return Mono.justOrEmpty(petitionRepo.deleteById(idx))
            .switchIfEmpty(Mono.error(HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "삭제 실패")))
            .flatMap { Mono.just(Unit) }
    }

}