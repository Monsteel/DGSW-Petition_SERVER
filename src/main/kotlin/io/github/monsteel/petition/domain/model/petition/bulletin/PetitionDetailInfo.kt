package io.github.monsteel.petition.domain.model.petition.bulletin

import java.util.*

class PetitionDetailInfo {
    var idx: Long? = null

    var writerID: String

    var createdAt: Date

    var expirationDate: Date

    var category: String

    var title: String

    var content: String


    var fKeyword: String? = null

    var sKeyword: String? = null

    var tKeyword: String? = null


    var agreeCount: Int? = null

    var isAnswer: Boolean = false

    constructor(
        idx: Long?,
        writerID: String,
        createdAt: Date,
        expirationDate: Date,
        category: String,
        title: String,
        content: String,
        fKeyword: String?,
        sKeyword: String?,
        tKeyword: String?,
        agreeCount: Int,
        isAnswer: Boolean
    ) {
        this.idx = idx
        this.writerID = writerID
        this.createdAt = createdAt
        this.expirationDate = expirationDate
        this.category = category
        this.title = title
        this.content = content
        this.fKeyword = fKeyword
        this.sKeyword = sKeyword
        this.tKeyword = tKeyword
        this.agreeCount = agreeCount
        this.isAnswer = isAnswer
    }
}

