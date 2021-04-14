package io.github.monsteel.petition.router

import io.github.monsteel.petition.handler.AuthHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Configuration
class AuthRouter(
    private val handler: AuthHandler
) {
    @Bean
    fun routerAuth() = nest(path("/auth"),
        router {
            listOf(
                GET("/register/check", handler::checkRegisteredUser),
                POST("/login", handler::login),
                POST("/register", handler::register)
            )
        }
    )
}