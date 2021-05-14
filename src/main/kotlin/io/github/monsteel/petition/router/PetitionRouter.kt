package io.github.monsteel.petition.router

import io.github.monsteel.petition.handler.PetitionHandler
import io.github.monsteel.petition.util.filter.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.router

@Configuration
class PetitionRouter(
    private val handler: PetitionHandler,
    private val jwtFilter: JwtFilter
) {
    @Bean
    fun routerPetition() = RouterFunctions.nest(RequestPredicates.path("/petition"),
        router {
            listOf(
                GET("", handler::getPetitions),
                GET("/ranks", handler::getPetitionRanking),
                GET("/search", handler::searchPetition),
                POST("", handler::writePetition),
                PUT("/{idx}", handler::editPetition),
                DELETE("/{idx}", handler::deletePetition)
            )
        }
    )
//            .filter(jwtFilter)
}