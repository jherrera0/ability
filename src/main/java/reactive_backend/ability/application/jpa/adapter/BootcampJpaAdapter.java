package reactive_backend.ability.application.jpa.adapter;

import lombok.AllArgsConstructor;
import reactive_backend.ability.application.jpa.entity.BootcampEntity;
import reactive_backend.ability.application.jpa.mapper.IBootcampEntityMapper;
import reactive_backend.ability.application.jpa.repository.IBootcampRepository;
import reactive_backend.ability.domain.model.Bootcamp;
import reactive_backend.ability.domain.spi.IBootcampPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
@AllArgsConstructor
public class BootcampJpaAdapter implements IBootcampPersistencePort {
    private final IBootcampRepository bootcampRepository;
    private final IBootcampEntityMapper bootcampEntityMapper;

    @Override
    public Flux<Bootcamp> addBootcamp(Integer bootcampId, List<Integer> abilityIds) {
        return Flux.fromIterable(abilityIds)
                .map(abilityId -> {
                    BootcampEntity relation = new BootcampEntity();
                    relation.setBootcampId(bootcampId);
                    relation.setAbilityId(abilityId);
                    return relation;
                })
                .flatMap(bootcampRepository::save).map(bootcampEntityMapper::toDomain);
    }

    @Override
    public Mono<List<Integer>> getAllAbilitiesByBootcampId(Integer id) {
        return bootcampRepository.findAllByBootcampId(id)
                .collectList()
                .map(bootcampEntityMapper::toDomainList)
                .map(bootcamps -> bootcamps.stream()
                        .map(Bootcamp::getAbilityId)
                        .toList());
    }
}
