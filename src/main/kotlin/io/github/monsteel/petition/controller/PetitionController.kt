package io.github.monsteel.petition.controller

import io.github.monsteel.petition.domain.dto.petition.bulletin.PetitionWriteDto
import io.github.monsteel.petition.domain.entity.petition.Petition
import io.github.monsteel.petition.domain.model.DataResponse
import io.github.monsteel.petition.domain.model.Response
import io.github.monsteel.petition.domain.model.petition.PetitionSimpleInfo
import io.github.monsteel.petition.service.petition.bulletin.PetitionServiceImpl
import io.github.monsteel.petition.util.enum.PetitionFetchType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/petition")
class PetitionController {
    @Autowired
    private lateinit var petitionService: PetitionServiceImpl

    /**
     * 청원 조회 API
     */
    @GetMapping("")
    fun getPetitions(@RequestParam(value="page") page:Int,
                     @RequestParam(value="size") size:Int,
                     @RequestParam(value="type", defaultValue = "3") type:Int): Response {

        val petitionSimpleArray = petitionService.fetchPetitions(page, size, PetitionFetchType.values().first { it.value == type }).map {
            val  agreeCount = 0 //TODO: 동의 조회
            val isAnswer = false //TODO: 답변 조회
            PetitionSimpleInfo(it.idx, it.expirationDate, it.category!!, it.title!!,agreeCount,isAnswer)
        }

        return DataResponse(HttpStatus.OK, "청원 조회 성공", petitionSimpleArray)
    }

    /**
     * 청원 작성 API
     */
    @PostMapping("")
    fun writePetition(@RequestBody petitionWriteDto: PetitionWriteDto): Response {
        petitionService.writePetition(petitionWriteDto)
        return Response(HttpStatus.OK, "청원 작성 완료")
    }

    /**
     * 청원 수정 API
     */
    @PutMapping("/{idx}")
    fun editPetition(@PathVariable("idx") idx: Long,
                     @RequestBody petitionWriteDto: PetitionWriteDto): Response {
        petitionService.editPetition(idx, petitionWriteDto)
        return Response(HttpStatus.OK, "청원 수정 완료")
    }

    /**
     * 청원 삭제 API
     */
    @DeleteMapping("/{idx}")
    fun deletePetition(@PathVariable("idx") idx: Long): Response {
        petitionService.deletePetition(idx)
        return Response(HttpStatus.OK, "청원 삭제 완료")
    }



    /**
     * 답변 조회 API
     */
    @GetMapping("/answer")
    fun getAnswer(): Response {
        //TODO
        return Response(HttpStatus.OK, "답변 조회 성공")
    }

    /**
     * 답변 등록 API
     */
    @PostMapping("/answer")
    fun addAnswer(): Response {
        //TODO
        return Response(HttpStatus.OK, "답변 등록 완료")
    }


    /**
     * 동의 조회 API
     */
    //TODO: Page 개념으로 접근
    @GetMapping("/agree")
    fun getAgree(): Response {
        //TODO
        return Response(HttpStatus.OK, "동의 조회 성공")
    }

    /**
     * 청원 동의 API
     */
    @PostMapping("/agree")
    fun agree(): Response {
        //TODO
        return Response(HttpStatus.OK, "청원 동의 완료")
    }
}