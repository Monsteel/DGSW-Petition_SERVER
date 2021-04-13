package io.github.monsteel.petition.domain.model.petition

import java.util.*

class PetitionSimpleInfo {
    var idx: Long? = null

    var expirationDate: Date

    var category: String

    var title: String

    var agreeCount: Int? = null

    var isAnswer: Boolean = false

    constructor(
        idx: Long?,
        expirationDate: Date,
        category: String,
        title: String,
        agreeCount: Int?,
        isAnswer: Boolean
    ) {
        this.idx = idx
        this.expirationDate = expirationDate
        this.category = category
        this.title = title
        this.agreeCount = agreeCount
        this.isAnswer = isAnswer
    }
}