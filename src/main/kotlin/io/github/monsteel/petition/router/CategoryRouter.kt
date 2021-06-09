package io.github.monsteel.petition.router

import io.github.monsteel.petition.handler.AuthHandler
import io.github.monsteel.petition.handler.CategoryHandler
import io.github.monsteel.petition.util.filter.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.router

@Configuration
class CategoryRouter(
        private val handler: CategoryHandler,
        private val jwtFilter: JwtFilter
) {
    @Bean
    fun routerCategory() = RouterFunctions.nest(RequestPredicates.path("/category"),
            router {
                listOf(
                        GET("", handler::getCategories),
                        GET("/{idx}", handler::getCategory)
                )
            }
    ).filter(jwtFilter)
}