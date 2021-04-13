package io.github.monsteel.petition.domain.model.petition.answer

import java.util.*

class AnswerDetailInfo {
    var idx: Long? = null
    var petitionIdx: Long
    var writerID: String
    var createdAt: Date
    var content:String

    constructor(idx: Long, petitionIdx: Long, writerID: String, createdAt: Date, content: String) {
        this.idx = idx
        this.petitionIdx = petitionIdx
        this.writerID = writerID
        this.createdAt = createdAt
        this.content = content
    }
}