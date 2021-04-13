package io.github.monsteel.petition.controller

import io.github.monsteel.petition.domain.dto.petition.agree.AgreeDto
import io.github.monsteel.petition.domain.dto.petition.answer.AnswerDto
import io.github.monsteel.petition.domain.dto.petition.bulletin.PetitionDto
import io.github.monsteel.petition.domain.model.DataResponse
import io.github.monsteel.petition.domain.model.Response
import io.github.monsteel.petition.domain.model.petition.answer.AnswerDetailInfo
import io.github.monsteel.petition.domain.model.petition.bulletin.PetitionSimpleInfo
import io.github.monsteel.petition.service.petition.agree.AgreeServiceImpl
import io.github.monsteel.petition.service.petition.answer.AnswerServiceImpl
import io.github.monsteel.petition.service.petition.bulletin.PetitionServiceImpl
import io.github.monsteel.petition.util.enum.PetitionFetchType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/petition")
class PetitionController {
    @Autowired
    private lateinit var petitionService: PetitionServiceImpl

    @Autowired
    private lateinit var answerService: AnswerServiceImpl

    @Autowired
    private lateinit var agreeService: AgreeServiceImpl

    /**
     * 청원 조회 API
     */
    @GetMapping("")
    fun getPetitions(@RequestParam(value="page") page:Int,
                     @RequestParam(value="size") size:Int,
                     @RequestParam(value="type", defaultValue = "3") type:Int): Response {

        val petitionSimpleArray = petitionService.fetchPetitions(page, size, PetitionFetchType.values().first { it.value == type }).map {
            val agreeCount = agreeService.fetchAgreeCount(it.idx!!)
            val isAnswer = answerService.fetchAnswer(it.idx!!).isNotEmpty()
            PetitionSimpleInfo(it.idx, it.expirationDate, it.category!!, it.title!!,agreeCount,isAnswer)
        }

        return DataResponse(HttpStatus.OK, "청원 조회 성공", petitionSimpleArray)
    }

    /**
     * 청원 검색 API
     */
    @GetMapping("/search")
    fun searchPetitions(@RequestParam(value="page") page:Int,
                        @RequestParam(value="size") size:Int,
                        @RequestParam(value="keyword") keyword:String): Response {

        val petitionSimpleArray = petitionService.searchPetition(page, size, keyword).map {
            val agreeCount = agreeService.fetchAgreeCount(it.idx!!)
            val isAnswer = answerService.fetchAnswer(it.idx!!).isNotEmpty()
            PetitionSimpleInfo(it.idx, it.expirationDate, it.category!!, it.title!!,agreeCount,isAnswer)
        }

        return DataResponse(HttpStatus.OK, "청원 검색 성공", petitionSimpleArray)
    }

    /**
     * 청원 작성 API
     */
    @PostMapping("")
    fun writePetition(@RequestBody petitionDto: PetitionDto): Response {
        petitionService.writePetition(petitionDto)
        return Response(HttpStatus.OK, "청원 작성 완료")
    }

    /**
     * 청원 수정 API
     */
    @PutMapping("/{idx}")
    fun editPetition(@PathVariable("idx") idx: Long,
                     @RequestBody petitionDto: PetitionDto): Response {
        petitionService.editPetition(idx, petitionDto)
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
    fun getAnswer(@RequestParam("petitionIdx") petitionIdx: Long): Response {
        val answerDetailInfoList = answerService.fetchAnswer(petitionIdx).map { AnswerDetailInfo(it.idx!!, it.petitionIdx!!, it.writerID!!, it.createdAt, it.content!!) }
        return DataResponse(HttpStatus.OK, "답변 조회 성공", answerDetailInfoList)
    }

    /**
     * 답변 등록 API
     */
    @PostMapping("/answer")
    fun addAnswer(@RequestBody answerDto: AnswerDto): Response {
        answerService.writeAnswer(answerDto)
        return Response(HttpStatus.OK, "답변 등록 완료")
    }


    /**
     * 동의 조회 API
     */
    //TODO: Page 개념으로 접근
    @GetMapping("/agree")
    fun getAgree(@RequestParam(value="page") page:Int,
                 @RequestParam(value="size") size:Int,
                 @RequestParam(value="petitionIdx") petitionIdx:Long): Response {
        val agreeList = agreeService.fetchAgree(page, size, petitionIdx)
        return DataResponse(HttpStatus.OK, "동의 조회 성공", agreeList)
    }

    /**
     * 청원 동의 API
     */
    @PostMapping("/agree")
    fun agree(@RequestBody agreeDto: AgreeDto): Response {
        agreeService.writeAgree(agreeDto)
        return Response(HttpStatus.OK, "청원 동의 완료")
    }
}