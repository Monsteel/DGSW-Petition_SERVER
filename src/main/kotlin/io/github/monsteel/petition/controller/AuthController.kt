package io.github.monsteel.petition.controller


import io.github.monsteel.petition.domain.dto.UserLoginDto
import io.github.monsteel.petition.domain.dto.UserRegisterDto
import io.github.monsteel.petition.domain.model.DataResponse
import io.github.monsteel.petition.domain.model.Response
import io.github.monsteel.petition.domain.model.auth.UserInquiry
import io.github.monsteel.petition.domain.model.auth.UserToken
import io.github.monsteel.petition.service.AuthService
import io.github.monsteel.petition.service.JwtService
import io.github.monsteel.petition.util.enum.JwtType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.lang.Exception
import javax.validation.Valid

@CrossOrigin
@RestController
@RequestMapping("/auth")
class AuthController {
    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var jwtService: JwtService

    /**
     * 회원가입 API
     */
    @PostMapping("/register")
    fun register(@RequestBody @Valid userRegisterDto: UserRegisterDto): Response {
        authService.register(userRegisterDto)
        return Response(HttpStatus.OK, "가입성공")
    }

    /**
     * 로그인 API
     */
    @PostMapping("/login")
    fun login(@RequestBody @Valid userLoginDto: UserLoginDto): Response {
        val userIdx: Long? = authService.login(userLoginDto)

        val accessToken: String = jwtService.createToken(userIdx!!, JwtType.ACCESS)
        val refreshToken: String = jwtService.createToken(userIdx, JwtType.REFRESH)
        val data = UserToken(accessToken, refreshToken)

        return DataResponse(HttpStatus.OK.value(), "로그인 성공", data)
    }

    /**
     * 가입여부 확인 API
     */
    @GetMapping("/register/check")
    fun checkRegisteredUser(@RequestParam("userId") userID: String): Response {
        val data = UserInquiry(false)

        try {
            authService.checkRegisteredUser(userID)
        } catch (e: Exception) {
            data.isRegistered = true
        }

        return DataResponse(HttpStatus.OK.value(), "조회 성공", data)
    }
}