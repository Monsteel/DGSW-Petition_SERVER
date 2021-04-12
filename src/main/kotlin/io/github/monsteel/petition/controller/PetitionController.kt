package io.github.monsteel.petition.controller

import io.github.monsteel.petition.domain.model.Response
import io.github.monsteel.petition.service.petition.bulletin.PetitionServiceImpl
import io.github.monsteel.petition.util.enum.PetitionFetchType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/petition")
@Controller
class PetitionController {
    @Autowired
    private lateinit var petitionService: PetitionServiceImpl

    /**
     * 청원 조회 API
     */
    //TODO: Page 개념으로 접근
    @GetMapping("")
    fun getPetitions(): Response {
        petitionService.fetchPetitions(1,20, PetitionFetchType.ON_GOING)
        return Response(HttpStatus.OK, "청원 조회 성공")
    }

    /**
     * 청원 작성 API
     */
    @PostMapping("")
    fun writePetition(): Response {
//        petitionService.writePetition()
        return Response(HttpStatus.OK, "청원 작성 완료")
    }

    /**
     * 청원 수정 API
     */
    @PutMapping("/")
    fun editPetition(): Response {
        //TODO
        return Response(HttpStatus.OK, "청원 수정 완료")
    }

    /**
     * 청원 삭제 API
     */
    @DeleteMapping("/")
    fun deletePetition(): Response {
        //TODO
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