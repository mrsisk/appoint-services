package mrsisk.github.io.appointservice.handlers;

import mrsisk.github.io.appointservice.dto.CreateAgentDto;
import mrsisk.github.io.appointservice.dto.InviteAgentDto;
import mrsisk.github.io.appointservice.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class AgentRouteHandler {

    @Autowired
    AgentService agentService;
    public Mono<ServerResponse> redeemInvite(ServerRequest request){

        return request.bodyToMono(CreateAgentDto.class)
                .flatMap(dto -> agentService.handleInvite(dto))
                .flatMap(agent -> ServerResponse.ok().bodyValue(agent))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(Map.of("error", e.getLocalizedMessage())));
    }

    public Mono<ServerResponse> invite(ServerRequest request){

        return request.bodyToMono(InviteAgentDto.class)
                .map(dto -> agentService.invite(dto))
                .map(token -> Map.of("token", token))
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(Map.of("error", e.getMessage())));
    }

    public Mono<ServerResponse> allAgents(ServerRequest request){
        var res  = agentService.getAgentsInternal();
        res.forEach(a -> System.out.println(a.getEmail()));
        return ServerResponse.ok().bodyValue(res);
    }
}
