package mrsisk.github.io.accountsmanager.service
import com.fasterxml.jackson.databind.ObjectMapper
import mrsisk.github.io.accountsmanager.models.Credentials
import mrsisk.github.io.accountsmanager.models.KeyCloakError
import mrsisk.github.io.accountsmanager.models.RegistrationBody
import mrsisk.github.io.accountsmanager.models.RegistrationRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.*

@Service
class RegistrationService(
    private val tokenFactory: TokenService,
    private val mailService: MailService,
    private val client: WebClient
) {

    fun inviteMember(team: String, email: String) {
        val claims = mapOf("eamil" to email, "team" to team)
        val token = tokenFactory.generateToken(email, claims)
        val url = "http://localhost:3000/team/register/$token"

        val message = "Please the link to continue with registration\n$url"
        mailService.sendSimpleMessage(email, "Registration", message)
    }

    suspend fun registerMember(body: RegistrationRequest): ClientResponse {
        //TODO VALIDATE TOKEN
//        val email = tokenFactory.getClaim("email", body.token)
//        val department = tokenFactory.getClaim("dpt", body.token)
//        //TODO VALIDATE IS BODY DETAILS SAME AS TOKEN DETAILS
//        if (email == null || department == null) throw Exception("Invalid token")
        val user = RegistrationBody(
            email = body.email,
            firstName = body.firstName,
            lastName = body.lastName,
            emailVerified = true,
            enabled = true,
            username = body.email,
            credentials = listOf(Credentials(value = body.password, temporary = false))
        )
        println(ObjectMapper().writeValueAsString(user))
        return client.post()
            .uri("/admin/realms/sisk/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(user)
            .awaitExchange {

                if (it.statusCode().is2xxSuccessful) ClientResponse.create(HttpStatus.CREATED)
                else if (it.statusCode() == HttpStatus.CONFLICT){
                    val error = it.awaitBody<KeyCloakError>()
                    return@awaitExchange ClientResponse.create(HttpStatus.CONFLICT).body(error.errorMessage)
                }
                else return@awaitExchange  ClientResponse.create(HttpStatus.BAD_REQUEST)
            }.build()



    }
}

data class Uzer(val id: String, val username: String)