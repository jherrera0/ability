package reactive_backend.ability.application.jpa.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactive_backend.ability.application.jpa.entity.AbilityEntity;
import reactor.core.publisher.Flux;

import java.util.Collection;

public interface IAbilityRepository extends ReactiveCrudRepository<AbilityEntity, Integer> {
    Flux<AbilityEntity> findAllBy(Pageable pageable);

    Flux<AbilityEntity> findAllByIdIsIn(Collection<Integer> ids);
}
