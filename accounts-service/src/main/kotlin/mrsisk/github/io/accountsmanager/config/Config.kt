package mrsisk.github.io.accountsmanager.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono


@Configuration
class Config {
    @Bean
    fun webClient(authorizedClientManager: ReactiveOAuth2AuthorizedClientManager): WebClient {
        val oauth2Client = ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager)
        oauth2Client.setDefaultClientRegistrationId("keycloak")
        return WebClient.builder()
            .filter(oauth2Client)
            .baseUrl("http://localhost:9001")
            .build()
    }

    @Bean
    fun authorizedClientManager(
        clientRegistrationRepository: ReactiveClientRegistrationRepository,
        authorizedClientRepository: ServerOAuth2AuthorizedClientRepository
    ): ReactiveOAuth2AuthorizedClientManager {
        val authorizedClientProvider: ReactiveOAuth2AuthorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
            .clientCredentials()
            .authorizationCode()
            .password()
            .refreshToken()
            .build()
        val authorizedClientManager = DefaultReactiveOAuth2AuthorizedClientManager(
            clientRegistrationRepository, authorizedClientRepository)
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider)
        return authorizedClientManager
    }

    //TODO
    @Bean
    fun userDetailedService(): ReactiveUserDetailsService = ReactiveUserDetailsService {username: String? ->
        Mono.just(User(username, "", emptyList()))
    }
}

