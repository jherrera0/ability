package reactive_backend.ability.domain.spi;

import reactive_backend.ability.domain.model.Ability;
import reactor.core.publisher.Mono;

public interface IAbilityPersistencePort {
    Mono<Ability> saveAbility(Ability ability);
}
