package mrsisk.github.io.appointservice.service;

import mrsisk.github.io.appointservice.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    WebClient client;

    @Value("${accounts.base-uri}")
    String baseUrl;

    public Mono<User> getUserByEmail(String email){
       return client.get()
                .uri(baseUrl + "/auth/admin", c -> c.path("/users")
                        .queryParam("email", email)
                        .build()).exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()){
                        return clientResponse.bodyToMono(User.class);
                    }
                    return Mono.empty();
                });
    }
}
