package io.github.monsteel.petition.domain.dto.petition.answer

import javax.validation.constraints.NotBlank

class AnswerDto {
    @NotBlank
    var petitionIdx: Long? = null

    @NotBlank
    var content:String? = null
}