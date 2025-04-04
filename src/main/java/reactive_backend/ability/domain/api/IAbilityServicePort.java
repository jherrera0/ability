package reactive_backend.ability.domain.api;

import reactive_backend.ability.domain.model.Ability;
import reactor.core.publisher.Mono;

public interface IAbilityServicePort {
    Mono<Ability> createAbility(Ability ability);
}
