package io.github.monsteel.petition.service.petition.bulletin

import io.github.monsteel.petition.domain.dto.petition.bulletin.PetitionDto
import io.github.monsteel.petition.domain.entity.petition.Petition
import io.github.monsteel.petition.domain.repository.petition.AnswerRepo
import io.github.monsteel.petition.domain.repository.petition.PetitionRepo
import io.github.monsteel.petition.util.enum.PetitionFetchType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class PetitionServiceImpl:PetitionService {
    @Autowired
    private lateinit var petitionRepo: PetitionRepo

    @Autowired
    private lateinit var answerRepo: AnswerRepo

    override fun fetchPetitions(page:Int, size:Int, type:PetitionFetchType): List<Petition> {
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())

        return when(type) {
            PetitionFetchType.ON_GOING ->
                petitionRepo.findValidPetition(pageable)

            PetitionFetchType.TERMINATED ->
                petitionRepo.findInvalidPetition(pageable)

            PetitionFetchType.FINISHED ->
                answerRepo.findAll().map { it.petitionIdx!! }.map { petitionRepo.findAllByIdx(it, pageable) }

            PetitionFetchType.ALL ->
                petitionRepo.findAll(pageable)
        }
    }

    override fun searchPetition(page:Int, size:Int, keyword:String): List<Petition> {
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        return petitionRepo.findAllByTitleContainingOrFKeywordContainingOrSKeywordContainingOrTKeywordContaining(keyword,keyword,keyword,keyword,pageable)
    }

    override fun fetchTopTenPetition(page:Int, size:Int): List<Petition> {
        return petitionRepo.findAll(PageRequest.of(0, 10, Sort.by("동의 수").descending()))
    }

    override fun writePetition(petitionDto: PetitionDto) {
        val petition = Petition()

        petition.init(
            petitionDto.category,
            petitionDto.title,
            petitionDto.content,
            petitionDto.fKeyword,
            petitionDto.sKeyword,
            petitionDto.tKeyword
        )

        petitionRepo.save(petition)
    }

    override fun editPetition(idx:Long, petitionDto: PetitionDto) {
        val petition = Petition()

        petition.init(
            petitionDto.category,
            petitionDto.title,
            petitionDto.content,
            petitionDto.fKeyword,
            petitionDto.sKeyword,
            petitionDto.tKeyword
        )

        petitionRepo.updatePetition(idx, petition)
    }

    override fun deletePetition(idx:Long) {
        petitionRepo.deleteById(idx)
    }

}