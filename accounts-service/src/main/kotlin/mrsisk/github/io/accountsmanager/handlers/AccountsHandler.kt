package mrsisk.github.io.accountsmanager.handlers

import kotlinx.coroutines.reactive.awaitFirst
import mrsisk.github.io.accountsmanager.models.InviteRequest
import mrsisk.github.io.accountsmanager.models.RegistrationRequest
import mrsisk.github.io.accountsmanager.service.RegistrationService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.awaitEntity
import org.springframework.web.reactive.function.server.*

@Component
class AccountsHandler(private val registrationService: RegistrationService) {

    suspend fun invite(serverRequest: ServerRequest): ServerResponse {
        val body = serverRequest.bodyToMono(InviteRequest::class.java).awaitFirst()
        registrationService.inviteMember(body.team, body.email)
        return ServerResponse.ok().buildAndAwait()

    }

    suspend fun register(serverRequest: ServerRequest): ServerResponse {
        val body = serverRequest.bodyToMono<RegistrationRequest>().awaitFirst()
        val clientResponse = registrationService.registerMember(body)
        val message  = clientResponse.awaitEntity<String>()
        return if (clientResponse.statusCode().is2xxSuccessful) ServerResponse.status(clientResponse.statusCode()).buildAndAwait()
        else ServerResponse.status(clientResponse.statusCode()).bodyValueAndAwait(mapOf("error" to message.body))
    }


}