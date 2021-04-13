package io.github.monsteel.petition.controller

import io.github.monsteel.petition.domain.dto.petition.agree.AgreeDto
import io.github.monsteel.petition.domain.dto.petition.answer.AnswerDto
import io.github.monsteel.petition.domain.dto.petition.bulletin.PetitionDto
import io.github.monsteel.petition.domain.model.DataResponse
import io.github.monsteel.petition.domain.model.Response
import io.github.monsteel.petition.domain.model.petition.answer.AnswerDetailInfo
import io.github.monsteel.petition.domain.model.petition.bulletin.PetitionSimpleInfo
import io.github.monsteel.petition.service.jwt.JwtServiceImpl
import io.github.monsteel.petition.service.petition.agree.AgreeServiceImpl
import io.github.monsteel.petition.service.petition.answer.AnswerServiceImpl
import io.github.monsteel.petition.service.petition.bulletin.PetitionServiceImpl
import io.github.monsteel.petition.util.Constant
import io.github.monsteel.petition.util.enum.PermissionType
import io.github.monsteel.petition.util.enum.PetitionFetchType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import javax.validation.Valid

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

    @Autowired
    private lateinit var jwtService: JwtServiceImpl

    /**
     * 청원 조회 API
     */
    @GetMapping("")
    fun getPetitions(@RequestHeader("x-access-token") token:String,
                     @RequestParam(value="page") page:Int,
                     @RequestParam(value="size") size:Int,
                     @RequestParam(value="type", defaultValue = "3") type:Int): Response {
        val user = jwtService.validateToken(token)

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
    fun searchPetitions(@RequestHeader("x-access-token") token:String,
                        @RequestParam(value="page") page:Int,
                        @RequestParam(value="size") size:Int,
                        @RequestParam(value="keyword") keyword:String): Response {
        val user = jwtService.validateToken(token)
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
    fun writePetition(@RequestHeader("x-access-token") token:String,
                      @RequestBody @Valid petitionDto: PetitionDto): Response {
        val user = jwtService.validateToken(token)
        petitionService.writePetition(petitionDto, user!!.userID!!)
        return Response(HttpStatus.OK, "청원 작성 완료")
    }

    /**
     * 청원 수정 API
     */
    @PutMapping("/{idx}")
    fun editPetition(@RequestHeader("x-access-token") token:String,
                     @PathVariable("idx") idx: Long,
                     @RequestBody @Valid petitionDto: PetitionDto): Response {
        val user = jwtService.validateToken(token)
        val agreeCount = agreeService.fetchAgreeCount(idx)

        if(agreeCount > Constant.DO_NOT_MODIFY_AGREE_COUNT && user!!.permissionType == PermissionType.STUDENT) {
            throw HttpClientErrorException(HttpStatus.UNAUTHORIZED, "최소 동의인원을 초과하여 수정할 수 없음")
        }

        petitionService.editPetition(idx, petitionDto, user!!.userID!!)
        return Response(HttpStatus.OK, "청원 수정 완료")
    }

    /**
     * 청원 삭제 API
     */
    @DeleteMapping("/{idx}")
    fun deletePetition(@RequestHeader("x-access-token") token:String,
                       @PathVariable("idx") idx: Long): Response {
        val user = jwtService.validateToken(token)
        val agreeCount = agreeService.fetchAgreeCount(idx)

        if(agreeCount > Constant.DO_NOT_MODIFY_AGREE_COUNT && user!!.permissionType == PermissionType.STUDENT) {
            throw HttpClientErrorException(HttpStatus.UNAUTHORIZED, "최소 동의인원을 초과하여 수정할 수 없음")
        }

        petitionService.deletePetition(idx, user!!.userID!!)
        return Response(HttpStatus.OK, "청원 삭제 완료")
    }



    /**
     * 답변 조회 API
     */
    @GetMapping("/answer")
    fun getAnswer(@RequestHeader("x-access-token") token:String,
                  @RequestParam("petitionIdx") petitionIdx: Long): Response {
        val user = jwtService.validateToken(token)
        val answerDetailInfoList = answerService.fetchAnswer(petitionIdx).map { AnswerDetailInfo(it.idx!!, it.petitionIdx!!, it.writerID!!, it.createdAt, it.content!!) }
        return DataResponse(HttpStatus.OK, "답변 조회 성공", answerDetailInfoList)
    }

    /**
     * 답변 등록 API
     */
    @PostMapping("/answer")
    fun addAnswer(@RequestHeader("x-access-token") token:String,
                  @RequestBody @Valid answerDto: AnswerDto): Response {
        val user = jwtService.validateToken(token)

        if(user!!.permissionType != PermissionType.EXECUTIVE){
            throw HttpClientErrorException(HttpStatus.UNAUTHORIZED, "권한 없음")
        }

        answerService.writeAnswer(answerDto,user.userID!!)
        return Response(HttpStatus.OK, "답변 등록 완료")
    }


    /**
     * 동의 조회 API
     */
    @GetMapping("/agree")
    fun getAgree(@RequestHeader("x-access-token") token:String,
                 @RequestParam(value="page") page:Int,
                 @RequestParam(value="size") size:Int,
                 @RequestParam(value="petitionIdx") petitionIdx:Long): Response {
        val user = jwtService.validateToken(token)
        val agreeList = agreeService.fetchAgree(page, size, petitionIdx)
        return DataResponse(HttpStatus.OK, "동의 조회 성공", agreeList)
    }

    /**
     * 청원 동의 API
     */
    @PostMapping("/agree")
    fun agree(@RequestHeader("x-access-token") token:String,
              @RequestBody @Valid agreeDto: AgreeDto): Response {
        val user = jwtService.validateToken(token)
        agreeService.writeAgree(agreeDto, user!!.userID!!)
        return Response(HttpStatus.OK, "청원 동의 완료")
    }
}