package io.github.monsteel.petition

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.*

@SpringBootApplication
class DgswPetitionServerApplication

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    Locale.setDefault(Locale.KOREA);
    runApplication<DgswPetitionServerApplication>(*args)
}
