package io.github.monsteel.petition.domain.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import java.lang.Exception

@Component
class OAuthRepo {
    fun fetchUserID(googleToken: String): String? {
        lateinit var rs: ResponseEntity<String>

        try {
            rs = RestTemplate().exchange("https://oauth2.googleapis.com/tokeninfo?id_token=$googleToken",
                    HttpMethod.GET,  null, String::class.java)
        } catch (e: HttpClientErrorException.BadRequest) {
            return null
        } catch (e: Exception) {
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "인증서버 연결 실패")
        }

        return try { Gson().fromJson(rs.body, JsonObject::class.java)["sub"].asString } catch (e: UninitializedPropertyAccessException) { null }
    }
}