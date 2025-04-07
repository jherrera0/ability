package reactive_backend.ability.application.jpa.adapter;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactive_backend.ability.application.jpa.mapper.IAbilityEntityMapper;
import reactive_backend.ability.application.jpa.repository.IAbilityRepository;
import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.model.PageCustom;
import reactive_backend.ability.domain.spi.IAbilityPersistencePort;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class AbilityJpaAdapter implements IAbilityPersistencePort {
    private final IAbilityRepository abilityRepository;
    private final IAbilityEntityMapper abilityEntityMapper;
    @Override
    public Mono<Ability> saveAbility(Ability ability) {
        return abilityRepository.save(abilityEntityMapper.toEntity(ability))
                .map(abilityEntityMapper::toDomain);
    }

    @Override
    public Mono<PageCustom<Ability>> getAllAbilities(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return abilityRepository.findAllBy(pageable)
                .collectList()
                .zipWith(abilityRepository.count())
                .map(tuple -> new PageCustom<>(
                        page,
                        size,
                        (int) Math.ceil((double) tuple.getT2() / size),
                        abilityEntityMapper.toDomainList(tuple.getT1())
                ));

    }

    @Override
    public Mono<List<Ability>>  getAbilitiesById(List<Integer> abilities) {
        return abilityRepository.findAllByIdIsIn(abilities)
                .collectList()
                .map(abilityEntityMapper::toDomainList);
    }
}
