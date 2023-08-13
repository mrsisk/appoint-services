package mrsisk.github.io.appointservice.routes;

import mrsisk.github.io.appointservice.handlers.AgentRouteHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class Routes {

    @Bean
    RouterFunction<ServerResponse> test(AgentRouteHandler agentRouteHandler){
        return route()
                .path("/api/v1/agent", builder -> {
                    builder.POST("/invite", agentRouteHandler::invite)
                            .POST(agentRouteHandler::redeemInvite)
                            .GET(agentRouteHandler::allAgents);
                }).build();
    }


}

