package reactive_backend.ability.application.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactive_backend.ability.application.jpa.entity.AbilityEntity;
import reactive_backend.ability.application.jpa.mapper.IAbilityEntityMapper;
import reactive_backend.ability.application.jpa.repository.IAbilityRepository;
import reactive_backend.ability.domain.model.Ability;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class AbilityJpaAdapterTest {

    @Mock
    private IAbilityRepository abilityRepository;

    @Mock
    private IAbilityEntityMapper abilityEntityMapper;

    @InjectMocks
    private AbilityJpaAdapter abilityJpaAdapter;

    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveAbility_successful() {
        Ability ability = new Ability();
        AbilityEntity abilityEntity = new AbilityEntity();

        when(abilityEntityMapper.toEntity(ability)).thenReturn(abilityEntity);
        when(abilityRepository.save(abilityEntity)).thenReturn(Mono.just(abilityEntity));
        when(abilityEntityMapper.toDomain(abilityEntity)).thenReturn(ability);

        Mono<Ability> result = abilityJpaAdapter.saveAbility(ability);

        StepVerifier.create(result)
                .expectNext(ability)
                .verifyComplete();
    }

}