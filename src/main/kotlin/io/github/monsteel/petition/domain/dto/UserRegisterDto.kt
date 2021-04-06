package io.github.monsteel.petition.domain.dto

import javax.validation.constraints.NotBlank

class UserRegisterDto {
    @NotBlank
    var permissionKey: String? = null

    @NotBlank
    val userID: String? = null

    @NotBlank
    var googleToken: String? = null
}