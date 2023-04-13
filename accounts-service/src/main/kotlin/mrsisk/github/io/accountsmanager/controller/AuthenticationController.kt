package mrsisk.github.io.accountsmanager.controller

import mrsisk.github.io.accountsmanager.config.RabbitMQConfig
import mrsisk.github.io.accountsmanager.models.AuthenticationResponse
import mrsisk.github.io.accountsmanager.models.LoginRequest
import mrsisk.github.io.accountsmanager.service.AuthService
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/auth/")
class AuthenticationController(private val authService: AuthService,private val template: RabbitTemplate) {

    @PostMapping("login")
    fun login(@RequestBody body: LoginRequest): Mono<AuthenticationResponse>{
        return  authService.authenticate(body.email, body.password)
    }
    @GetMapping("test")
    fun test(): String{
        template.convertAndSend(RabbitMQConfig.internalExchange, RabbitMQConfig.routingKey, mapOf("name" to "Sisk"))
        return "hello"
    }



}