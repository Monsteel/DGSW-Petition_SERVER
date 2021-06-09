package io.github.monsteel.petition.domain.model.petition.bulletin

import io.github.monsteel.petition.util.extension.toISOString
import java.util.*

class PetitionDetailInfo {
    var idx: Long? = null

    var writerID: String

    var createdAt: String

    var expirationDate: String

    var category: Int

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
        category: Int,
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
        this.createdAt = createdAt.toISOString()
        this.expirationDate = expirationDate.toISOString()
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

