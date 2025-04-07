package reactive_backend.ability.domain.api;

import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.model.PageCustom;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IAbilityServicePort {
    Mono<Ability> createAbility(Ability ability);
    Mono<PageCustom<Ability>> getAllAbilities(Integer page, Integer size, String sortDirection, String sortField);

    Mono<List<Ability>> getAbilityById(List<Integer> ids);
}
