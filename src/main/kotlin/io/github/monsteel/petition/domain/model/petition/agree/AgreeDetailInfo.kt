package io.github.monsteel.petition.domain.model.petition.agree

import java.util.*

class AgreeDetailInfo {
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