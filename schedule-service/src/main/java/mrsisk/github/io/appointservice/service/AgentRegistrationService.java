package mrsisk.github.io.appointservice.service;

import mrsisk.github.io.appointservice.dto.AgentRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AgentRegistrationService {

    @Autowired
    WebClient client;

    @Value("${accounts.base-uri}")
    String baseUrl;

    public Mono<HttpStatusCode> register(AgentRegistrationDto dto){
        return client.post()
                .uri(baseUrl + "/auth/admin/agents")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchangeToMono(clientResponse -> {
                    System.out.println("RESPONSE FROM AGENT REG CODE  " + clientResponse.statusCode());
                    return Mono.just(clientResponse.statusCode());
                });
    }


}
