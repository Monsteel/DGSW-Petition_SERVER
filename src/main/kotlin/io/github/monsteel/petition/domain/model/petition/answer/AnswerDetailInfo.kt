package io.github.monsteel.petition.domain.model.petition.answer

import java.util.*

class AnswerDetailInfo {
    var idx: Long? = null
    var petitionIdx: Long
    var userID: String
    var createdAt: Date
    var content:String

    constructor(idx: Long, petitionIdx: Long, userID: String, createdAt: Date, content: String) {
        this.idx = idx
        this.petitionIdx = petitionIdx
        this.userID = userID
        this.createdAt = createdAt
        this.content = content
    }
}