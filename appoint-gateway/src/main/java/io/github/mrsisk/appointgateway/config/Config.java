package io.github.mrsisk.appointgateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${accounts.url}")
    String accountUrl;

    @Value("${api.url}")
    String apiUrl;
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return  builder
                .routes()
                .route("appoint-accounts", predicateSpec ->
                    predicateSpec.path("/accounts")
                            .uri(accountUrl+"/**")
                ).route("appoint-api", predicateSpec ->
                        predicateSpec.path("/api/v1/**")
                                .uri(apiUrl+"/**")
                ).build();
    }
}
