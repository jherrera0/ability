package reactive_backend.ability.domain.api;

import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.model.Bootcamp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootcampServicePort {
    Flux<Bootcamp> addBootcamp(Integer bootcampId, List<Ability> abilityIds);
    Mono<List<Ability>> getAllAbilitiesByBootcampId(Integer id);
}
