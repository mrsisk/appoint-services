package mrsisk.github.io.accountsmanager.routes

import mrsisk.github.io.accountsmanager.handlers.AccountsHandler
import mrsisk.github.io.accountsmanager.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouterConfiguration {

    @Bean
    fun adminRoutes(userService: UserService) = coRouter {
        GET("/admin/users", userService::findAllUsers)
        GET("/admin/users/{id}", userService::findUser)
    }

    @Bean
    fun accountRoutes(handler: AccountsHandler) = coRouter {
        POST("/user/invite", handler::invite)
        POST("/user/register", handler::register)
       // POST("/user/register", handler::test) //verify token
    }
}