package io.github.monsteel.petition.domain.model

import org.springframework.http.HttpStatus

open class Response(status: HttpStatus, val message: String) {
    val status: Int = status.value()
}
