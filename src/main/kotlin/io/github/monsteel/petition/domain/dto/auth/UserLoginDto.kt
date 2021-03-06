package io.github.monsteel.petition.domain.dto.auth

import javax.validation.constraints.NotBlank

class UserLoginDto {
    @NotBlank
    val userID: String? = null

    @NotBlank
    var googleToken: String? = null
}