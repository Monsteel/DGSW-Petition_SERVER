package io.github.monsteel.petition.domain.dto.petition.bulletin

import javax.validation.constraints.NotBlank

class PetitionDto {
    @NotBlank
    var category: String? = null

    @NotBlank
    var title:String? = null

    @NotBlank
    var content:String? = null

    var firstKeyword:String? = null

    var secondKeyword:String? = null

    var thirdKeyword:String? = null
}