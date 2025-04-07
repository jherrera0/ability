package reactive_backend.ability.domain.spi;

import reactive_backend.ability.domain.model.Bootcamp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootcampPersistencePort {
    Flux<Bootcamp> addBootcamp(Integer bootcampId, List<Integer> abilityIds);
    Mono<List<Integer>> getAllAbilitiesByBootcampId(Integer id);
}
