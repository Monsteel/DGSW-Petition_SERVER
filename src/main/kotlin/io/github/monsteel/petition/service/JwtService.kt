package io.github.monsteel.petition.service

import io.github.monsteel.petition.domain.entity.User
import io.github.monsteel.petition.util.enum.JwtType

interface JwtService {
    fun createToken(idx: Long, typeType: JwtType): String
    fun validateToken(token: String?): User?
    fun refreshToken(refreshToken: String?): String?
}