package io.github.monsteel.petition.domain.dto.petition.agree

import javax.validation.constraints.NotBlank

class AgreeDto {
    @NotBlank
    var petitionIdx: Long? = null

    @NotBlank
    var content:String? = null
}