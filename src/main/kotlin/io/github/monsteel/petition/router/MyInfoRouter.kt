package io.github.monsteel.petition.router

import io.github.monsteel.petition.handler.MyInfoHandler
import io.github.monsteel.petition.util.filter.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.router

@Configuration
class MyInfoRouter(
        private val handler: MyInfoHandler
) {
    @Bean
    fun routerMyInfo() = RouterFunctions.nest(RequestPredicates.path("/myinfo"),
            router {
                listOf(
                        GET("", handler::getMyInfo)
                )
            }
    )
}