package io.github.monsteel.petition.service.auth

import io.github.monsteel.petition.domain.dto.auth.UserLoginDto
import io.github.monsteel.petition.domain.dto.auth.UserRegisterDto
import io.github.monsteel.petition.domain.entity.User
import reactor.core.publisher.Mono

interface AuthService {
    fun register(userRegisterDto: UserRegisterDto): Mono<Unit>
    fun login(userLoginDto: UserLoginDto): Mono<User>
    fun checkValidRequest(googleToken:String?, userID:String?): Mono<String>
    fun checkRegisteredUser(userID: String?): Mono<Unit>
}