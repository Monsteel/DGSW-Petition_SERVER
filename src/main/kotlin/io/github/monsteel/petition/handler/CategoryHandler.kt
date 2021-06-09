package io.github.monsteel.petition.handler

import io.github.monsteel.petition.domain.dto.auth.UserRegisterDto
import io.github.monsteel.petition.domain.model.DataResponse
import io.github.monsteel.petition.domain.model.Response
import io.github.monsteel.petition.domain.model.petition.category.CategoryInfo
import io.github.monsteel.petition.service.category.CategoryService
import io.github.monsteel.petition.service.category.CategoryServiceImpl
import io.github.monsteel.petition.service.jwt.JwtServiceImpl
import io.github.monsteel.petition.service.petition.answer.AnswerServiceImpl
import io.github.monsteel.petition.util.extension.toServerResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class CategoryHandler(
        private val categoryService: CategoryServiceImpl
) {
    fun getCategories(request: ServerRequest): Mono<ServerResponse> =
            categoryService.getCategories()
                    .flatMap { categories ->
                        Mono.just(categories.map { CategoryInfo(it.idx,it.categoryName) })
                    }
                    .flatMap { DataResponse(HttpStatus.OK, "카테고리 조회 성공", it).toServerResponse() }
                    .onErrorResume { it.toServerResponse() }

    fun getCategory(request: ServerRequest): Mono<ServerResponse> =
            Mono.just(request.pathVariable("idx").toLong())
                    .flatMap { categoryService.getCategory(it) }
                    .flatMap { Mono.just(CategoryInfo(it.idx,it.categoryName)) }
                    .flatMap { DataResponse(HttpStatus.OK, "카테고리 조회 성공", it).toServerResponse() }
                    .onErrorResume { it.toServerResponse() }
}