package mrsisk.github.io.accountsmanager.service

import mrsisk.github.io.accountsmanager.models.AuthenticationResponse
import mrsisk.github.io.accountsmanager.models.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

const val OPEN_ID_URL = "/realms/sisk/protocol/openid-connect/token"

@Service
class AuthService(private val client: WebClient) {

    @Value("\${accounts-manager.oauth.client-id}")
    lateinit var clientId: String

    @Value("\${accounts-manager.oauth.client-secret}")
    lateinit var clientSecret: String


    fun authenticate(email: String, password: String): Mono<AuthenticationResponse>{
        println("-------- $clientId and sec is $clientSecret")
        val formData = LinkedMultiValueMap<String, String>()
        formData.add("username", email)
        formData.add("password", password)
        formData.add("grant_type", "password")
        formData.add("client_id", clientId)
        formData.add("client_secret", clientSecret)

        try {
            println("logging user $formData")
            return client.post()
                .uri(OPEN_ID_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(AuthenticationResponse::class.java)
        } catch (ex: Exception) {
            println("Error: " + ex.localizedMessage)
            throw ex
        }

    }

    fun test(): Flux<User> {

        return client.get()
            .uri("/admin/realms/sisk/users")

            .exchangeToFlux {response ->
                if (response.statusCode().is2xxSuccessful) return@exchangeToFlux response.bodyToFlux(User::class.java)
                else throw Exception("Error")

            }
    }
}