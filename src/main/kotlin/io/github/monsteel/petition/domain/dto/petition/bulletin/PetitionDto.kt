package io.github.monsteel.petition.domain.dto.petition.bulletin

import javax.validation.constraints.NotBlank

class PetitionDto {
    @NotBlank
    var category: String? = null

    @NotBlank
    var title:String? = null

    @NotBlank
    var content:String? = null

    var fKeyword:String? = null

    var sKeyword:String? = null

    var tKeyword:String? = null


}