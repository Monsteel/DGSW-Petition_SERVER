package io.github.monsteel.petition.router

import io.github.monsteel.petition.handler.TokenHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.router

@Configuration
class TokenRouter(
    private val handler: TokenHandler
) {
    @Bean
    fun routerToken() = RouterFunctions.nest(RequestPredicates.path("/token"),
        router {
            listOf(
                POST("/refresh", handler::refreshToken),
            )
        }
    )
}