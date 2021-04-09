package io.github.monsteel.petition.service.auth

import io.github.monsteel.petition.domain.dto.auth.UserLoginDto
import io.github.monsteel.petition.domain.dto.auth.UserRegisterDto

interface AuthService {
    fun register(userRegisterDto: UserRegisterDto)
    fun login(userLoginDto: UserLoginDto): Long?
    fun checkValidRequest(googleToken: String?, userID: String?)
    fun checkRegisteredUser(userID: String?)
}