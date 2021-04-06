package io.github.monsteel.petition.domain.model.auth

data class UserToken(var accessToken: String,
                     var refreshToken: String)