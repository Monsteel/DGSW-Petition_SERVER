package io.github.monsteel.petition.service.petition.bulletin

import io.github.monsteel.petition.domain.dto.petition.bulletin.PetitionDto
import io.github.monsteel.petition.domain.entity.User
import io.github.monsteel.petition.domain.entity.petition.Category
import io.github.monsteel.petition.domain.entity.petition.Petition
import io.github.monsteel.petition.domain.model.petition.bulletin.PetitionSimpleInfo
import io.github.monsteel.petition.domain.repository.petition.AgreeRepo
import io.github.monsteel.petition.domain.repository.petition.AnswerRepo
import io.github.monsteel.petition.domain.repository.petition.CategoryRepo
import io.github.monsteel.petition.domain.repository.petition.PetitionRepo
import io.github.monsteel.petition.service.jwt.JwtServiceImpl
import io.github.monsteel.petition.util.Constant
import io.github.monsteel.petition.util.enum.PetitionFetchType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.temporal.TemporalAmount

@Service
class PetitionServiceImpl(
    private val categoryRepo: CategoryRepo,
    private val petitionRepo: PetitionRepo,
    private val answerRepo: AnswerRepo,
    private val agreeRepo: AgreeRepo
):PetitionService {
    override fun fetchPetitionDetailInfo(idx: Long): Mono<Petition> {
        return Mono.just(petitionRepo.findByIdx(idx))
    }

    override fun fetchPetitions(page:Int, size:Int, type:PetitionFetchType): Mono<List<Petition>> {
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())

        return when(type) {
            PetitionFetchType.ON_GOING ->
                Mono.just(petitionRepo.findAllByExpirationDateAfter(pageable))

            PetitionFetchType.TERMINATED ->
                Mono.just(petitionRepo.findAllByExpirationDateBefore(pageable))

            PetitionFetchType.FINISHED ->
                Mono.just(answerRepo.findAll(pageable).toList().map { petitionRepo.findByIdx(it.petition!!.idx!!) })

            PetitionFetchType.ALL ->
                Mono.just(petitionRepo.findAll(pageable).toList())

            PetitionFetchType.AWAITING ->
                Flux.fromIterable(agreeRepo.findAwaitingPetitionIdx(PageRequest.of(page, size))).flatMap { Mono.just(petitionRepo.findByIdx(it)) }.collectList()
        }
    }

    override fun searchPetition(page:Int, size:Int, keyword:String): Mono<List<Petition>> {
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        return Mono.just(petitionRepo.findAllByTitleContainsOrFirstKeywordContainsOrSecondKeywordContainsOrThirdKeywordContains(keyword,keyword,keyword,keyword,pageable))
    }

    override fun fetchPetitionRanking(amount: Int): Mono<List<Petition>> {
        return Mono.just(agreeRepo.findRanking(amount).map { petitionRepo.findByIdx(it.petitionIdx) })
    }

    override fun fetchAwaitingPetition(): Mono<List<Petition>> {
        return Mono.just(agreeRepo.findAwaitingPetitionIdx().map { petitionRepo.findByIdx(it) })
    }

    override fun fetchAwaitingPetitionCount(): Mono<Int> {
        return Mono.just(agreeRepo.findAwaitingPetitionIdx().count())
    }

    override fun writePetition(petitionDto: PetitionDto, user: User): Mono<Unit> {

        val category = Category()
        category.idx = petitionDto.category!!.toLong()

        return Mono.justOrEmpty(petitionRepo.save(Petition(user,category, petitionDto.title,
                petitionDto.content, petitionDto.firstKeyword, petitionDto.secondKeyword, petitionDto.thirdKeyword))
        ).switchIfEmpty(Mono.error(HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "작성 실패")))
         .flatMap { Mono.just(Unit) }
    }

    override fun editPetition(idx:Long, petitionDto: PetitionDto, user: User): Mono<Unit> {
        val updatePetition = petitionRepo.findByIdx(idx)

        if(updatePetition.user!!.userID != user.userID) {
            throw HttpClientErrorException(HttpStatus.UNAUTHORIZED, "권한 없음")
        }

        val category = Category()
        category.idx = petitionDto.category!!.toLong()

        updatePetition.mod(
            category,
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

    override fun deletePetition(idx:Long,  user: User):Mono<Unit> {
        val deletePetition = petitionRepo.findByIdx(idx)

        if(deletePetition.user!!.userID != user.userID) {
            throw HttpClientErrorException(HttpStatus.UNAUTHORIZED, "권한 없음")
        }

        return Mono.justOrEmpty(petitionRepo.deleteById(idx))
            .switchIfEmpty(Mono.error(HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "삭제 실패")))
            .flatMap { Mono.just(Unit) }
    }

}