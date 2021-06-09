package io.github.monsteel.petition.domain.model.petition.bulletin

import io.github.monsteel.petition.util.extension.toISOString
import java.util.*

class PetitionSimpleInfo {
    var idx: Long? = null

    var expirationDate: String

    var category: Int

    var title: String

    var agreeCount: Int? = null

    var isAnswer: Boolean = false

    constructor(
        idx: Long?,
        expirationDate: Date,
        category: Int,
        title: String,
        agreeCount: Int?,
        isAnswer: Boolean
    ) {
        this.idx = idx
        this.expirationDate = expirationDate.toISOString()
        this.category = category
        this.title = title
        this.agreeCount = agreeCount
        this.isAnswer = isAnswer
    }
}