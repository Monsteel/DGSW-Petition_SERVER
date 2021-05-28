package io.github.monsteel.petition.domain.model.petition.agree

import io.github.monsteel.petition.util.extension.toISOString
import java.util.*

class AgreeDetailInfo {
    var idx: Long? = null
    var petitionIdx: Long
    var userID: String
    var createdAt: String
    var content:String

    constructor(idx: Long, petitionIdx: Long, userID: String, createdAt: Date, content: String) {
        this.idx = idx
        this.petitionIdx = petitionIdx
        this.userID = userID
        this.createdAt = createdAt.toISOString()
        this.content = content
    }
}