package io.github.monsteel.petition

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DgswPetitionServerApplication

fun main(args: Array<String>) {
    runApplication<DgswPetitionServerApplication>(*args)
}
