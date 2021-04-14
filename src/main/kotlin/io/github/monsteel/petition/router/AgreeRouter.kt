package io.github.monsteel.petition.router

import io.github.monsteel.petition.handler.AgreeHandler
import io.github.monsteel.petition.util.filter.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.router

@EnableWebFlux
@Configuration
class AgreeRouter(
    private val handler: AgreeHandler,
    private val jwtFilter: JwtFilter
) {
    @Bean
    fun routerAgree() = RouterFunctions.nest(RequestPredicates.path("/agree"),
        router {
            listOf(
                GET("", handler::getAgree),
                POST("", handler::agree)
            )
        }
    ).filter(jwtFilter)
}