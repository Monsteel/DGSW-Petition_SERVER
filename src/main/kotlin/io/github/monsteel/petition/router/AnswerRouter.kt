package io.github.monsteel.petition.router

import io.github.monsteel.petition.handler.AnswerHandler
import io.github.monsteel.petition.util.filter.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.path
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.router

@Configuration
class AnswerRouter(
    private val handler: AnswerHandler,
    private val jwtFilter: JwtFilter
) {
    @Bean
    fun routerAnswer() = nest(path("/answer"),
        router {
            listOf(
                GET("", handler::getAnswer),
                POST("", handler::addAnswer)
            )
        }
    ).filter(jwtFilter)
}