package io.github.monsteel.petition.domain.model

import org.springframework.http.HttpStatus

class DataResponse<T>(status: HttpStatus, message: String, val data: T): Response(status, message)