package io.github.monsteel.petition.router

import io.github.monsteel.petition.handler.AuthHandler
import io.github.monsteel.petition.handler.CategoryHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.router

@Configuration
class CategoryRouter(
        private val handler: CategoryHandler
) {
    @Bean
    fun routerCategory() = RouterFunctions.nest(RequestPredicates.path("/category"),
            router {
                listOf(
                        GET("", handler::getCategories),
                        GET("/{idx}", handler::getCategory)
                )
            }
    )
}