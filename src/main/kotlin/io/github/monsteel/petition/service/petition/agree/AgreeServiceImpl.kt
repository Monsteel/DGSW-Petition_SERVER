package io.github.monsteel.petition.service.petition.agree

import io.github.monsteel.petition.domain.dto.petition.agree.AgreeDto
import io.github.monsteel.petition.domain.dto.petition.answer.AnswerDto
import io.github.monsteel.petition.domain.entity.petition.Agree
import io.github.monsteel.petition.domain.repository.petition.AgreeRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException

@Service
class AgreeServiceImpl:AgreeService {
    @Autowired
    private lateinit var agreeRepo: AgreeRepo

    override fun fetchAgreeCount(petitionIdx:Long): Int {
        return agreeRepo.findAllByPetitionIdx(petitionIdx).count()
    }

    override fun fetchAgree(page:Int, size:Int, petitionIdx:Long): List<Agree> {
        val pageable = PageRequest.of(page, size, Sort.by("idx").descending())
        return agreeRepo.findAllByPetitionIdx(petitionIdx, pageable)
    }

    override fun writeAgree(agreeDto: AgreeDto, userID:String) {
        val agree = Agree()

        val isAgree = agreeRepo.findAllByPetitionIdxAndWriterID(agreeDto.petitionIdx!!, userID).isNotEmpty()

        if(isAgree){
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "이미 동의한 청원")
        }

        agree.petitionIdx = agreeDto.petitionIdx
        agree.content = agreeDto.content
        agree.writerID = userID

        agreeRepo.save(agree)
    }
}