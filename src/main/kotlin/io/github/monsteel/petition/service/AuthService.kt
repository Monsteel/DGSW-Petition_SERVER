package io.github.monsteel.petition.service

import io.github.monsteel.petition.domain.dto.UserLoginDto
import io.github.monsteel.petition.domain.dto.UserRegisterDto

interface AuthService {
    fun register(userRegisterDto: UserRegisterDto)
    fun login(userLoginDto: UserLoginDto): Long?
    fun checkValidRequest(googleToken: String?, userID: String?)
    fun checkRegisteredUser(userID: String?)
}