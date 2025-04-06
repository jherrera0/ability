package reactive_backend.ability.domain.spi;

import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.model.PageCustom;
import reactor.core.publisher.Mono;

public interface IAbilityPersistencePort {
    Mono<Ability> saveAbility(Ability ability);
    Mono<PageCustom<Ability>> getAllAbilities(Integer page, Integer size);
}
