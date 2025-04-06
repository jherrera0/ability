package reactive_backend.ability.application.jpa.adapter;

import lombok.AllArgsConstructor;
import reactive_backend.ability.application.jpa.mapper.IAbilityEntityMapper;
import reactive_backend.ability.application.jpa.repository.IAbilityRepository;
import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.spi.IAbilityPersistencePort;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class AbilityJpaAdapter implements IAbilityPersistencePort {
    private final IAbilityRepository abilityRepository;
    private final IAbilityEntityMapper abilityEntityMapper;
    @Override
    public Mono<Ability> saveAbility(Ability ability) {
        return abilityRepository.save(abilityEntityMapper.toEntity(ability))
                .map(abilityEntityMapper::toDomain);
    }
}
