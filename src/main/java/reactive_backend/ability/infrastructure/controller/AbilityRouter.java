package reactive_backend.ability.infrastructure.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactive_backend.ability.application.http.handler.IAbilityHandler;
import reactive_backend.ability.domain.util.ConstRoute;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AbilityRouter {

    @Bean
    public RouterFunction<ServerResponse> abilityRoutes(IAbilityHandler abilityHandler) {
        return route(POST(ConstRoute.ABILITY_REST_ROUTE + ConstRoute.CREATE_ABILITY_REST_ROUTE),
                abilityHandler::createAbility)
                .andRoute(POST(ConstRoute.ABILITY_REST_ROUTE + ConstRoute.LIST_ABILITY_REST_ROUTE),
                        abilityHandler::getAllAbilities);
    }
}
