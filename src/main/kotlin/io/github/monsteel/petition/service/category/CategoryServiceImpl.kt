package io.github.monsteel.petition.service.category

import io.github.monsteel.petition.domain.entity.petition.Category
import io.github.monsteel.petition.domain.repository.UserRepo
import io.github.monsteel.petition.domain.repository.petition.CategoryRepo
import io.github.monsteel.petition.service.jwt.JwtService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CategoryServiceImpl(
        private val categoryRepo: CategoryRepo
): CategoryService {
    override fun getCategories(): Mono<List<Category>> {
        return Mono.just(categoryRepo.findAll())
    }

    override fun getCategory(idx: Long): Mono<Category> {
        return Mono.just(categoryRepo.getByIdx(idx))
    }
}