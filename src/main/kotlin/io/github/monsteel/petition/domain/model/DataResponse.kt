package io.github.monsteel.petition.domain.model

import org.springframework.http.HttpStatus

data class DataResponse<T>(override var status: Int,
                           override var message: String,
                           val data: T) : Response() {
    constructor(status: HttpStatus, message: String) {
        this.status = status.value()
        this.message = message
    }
}