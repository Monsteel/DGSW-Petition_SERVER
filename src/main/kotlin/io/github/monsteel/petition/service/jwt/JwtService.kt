package io.github.monsteel.petition.service.jwt

import io.github.monsteel.petition.domain.entity.User
import io.github.monsteel.petition.util.enum.JwtType
import reactor.core.publisher.Mono

interface JwtService {
    fun createToken(idx: Long, typeType: JwtType): String
    fun validateToken(token: String?): Mono<User?>
    fun refreshToken(refreshToken: String?): Mono<String?>
}