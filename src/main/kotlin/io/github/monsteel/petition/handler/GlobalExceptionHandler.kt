package io.github.monsteel.petition.handler

import io.github.monsteel.petition.domain.model.Response
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import java.lang.Exception
import javax.servlet.http.HttpServletResponse

import javax.servlet.http.HttpServletRequest

@ControllerAdvice
@ResponseBody
class GlobalExceptionHandler {
    @ExceptionHandler(HttpClientErrorException::class)
    fun handleHttpClientErrorException(req: HttpServletRequest, res: HttpServletResponse, e: HttpClientErrorException): Response? {
        res.status = e.statusCode.value()
        return Response(e.statusCode, e.statusText)
    }

    @ExceptionHandler(HttpServerErrorException::class)
    fun handleHttpServerErrorException(req: HttpServletRequest, res: HttpServletResponse, e: HttpServerErrorException): Response? {
        res.status = e.statusCode.value()
        return Response(e.statusCode, e.statusText)
    }

    @ExceptionHandler(Exception::class)
    fun handleHttpGlobalException(req: HttpServletRequest, res: HttpServletResponse, e: HttpServerErrorException): Response? {
        res.status = 500
        return Response(e.statusCode, e.statusText)
    }
}