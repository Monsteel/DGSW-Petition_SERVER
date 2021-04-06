package io.github.monsteel.petition.domain.model

import org.springframework.http.HttpStatus

open class Response(status: HttpStatus,
                    open var message: String){
    open var status:Int = status.value()
    constructor(){}
}