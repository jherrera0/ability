package reactive_backend.ability.domain.usecase;

import reactive_backend.ability.domain.api.IBootcampServicePort;
import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.model.Bootcamp;
import reactive_backend.ability.domain.spi.IAbilityPersistencePort;
import reactive_backend.ability.domain.spi.IBootcampPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class BootcampCase implements IBootcampServicePort {
    private final IBootcampPersistencePort bootcampPersistencePort;
    private final IAbilityPersistencePort abilityPersistencePort;

    public BootcampCase(IBootcampPersistencePort bootcampPersistencePort,
                        IAbilityPersistencePort abilityPersistencePort) {
        this.bootcampPersistencePort = bootcampPersistencePort;
        this.abilityPersistencePort = abilityPersistencePort;
    }

    @Override
    public Flux<Bootcamp> addBootcamp(Integer bootcampId, List<Ability> abilities) {
        List<Integer> abilityIdList = abilities.stream()
                .map(Ability::getId)
                .toList();
        return bootcampPersistencePort.addBootcamp(bootcampId, abilityIdList);
    }

    @Override
    public Mono<List<Ability>> getAllAbilitiesByBootcampId(Integer id) {
        return bootcampPersistencePort.getAllAbilitiesByBootcampId(id)
                .flatMap(abilities -> abilityPersistencePort.getAbilitiesById(abilities)
                        .flatMap(Mono::just));
    }
}
