package reactive_backend.ability.domain.spi;

import reactive_backend.ability.domain.model.Technology;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyClientPort {
    Mono<List<Technology>> findTechnologiesByNames(List<String> names);

    Mono<Void> linkTechnologiesToAbility(Integer id, List<Technology> technologies);
}
