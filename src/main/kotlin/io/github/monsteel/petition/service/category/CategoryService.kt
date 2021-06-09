package io.github.monsteel.petition.service.category

import io.github.monsteel.petition.domain.entity.petition.Category
import reactor.core.publisher.Mono

interface CategoryService {
    fun getCategories(): Mono<List<Category>>
    fun getCategory(idx: Long): Mono<Category>
}