package reactive_backend.ability.application.jpa.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactive_backend.ability.application.jpa.entity.BootcampEntity;
import reactor.core.publisher.Flux;

public interface IBootcampRepository extends ReactiveCrudRepository<BootcampEntity, Integer> {
    Flux<BootcampEntity> findAllByBootcampId(Integer id);
}
