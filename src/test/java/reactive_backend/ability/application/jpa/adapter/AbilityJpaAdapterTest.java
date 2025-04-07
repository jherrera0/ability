package reactive_backend.ability.application.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactive_backend.ability.application.jpa.entity.AbilityEntity;
import reactive_backend.ability.application.jpa.mapper.IAbilityEntityMapper;
import reactive_backend.ability.application.jpa.repository.IAbilityRepository;
import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.model.PageCustom;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

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
    @Test
    void getAllAbilities_shouldReturnPageCustom_whenPageAndSizeAreValid() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        List<AbilityEntity> abilityEntities = List.of(new AbilityEntity());
        List<Ability> abilities = List.of(new Ability());
        PageCustom<Ability> expectedPage = new PageCustom<>(page, size, 1, abilities);

        when(abilityRepository.findAllBy(pageable)).thenReturn(Flux.fromIterable(abilityEntities));
        when(abilityRepository.count()).thenReturn(Mono.just(1L));
        when(abilityEntityMapper.toDomainList(abilityEntities)).thenReturn(abilities);

        Mono<PageCustom<Ability>> result = abilityJpaAdapter.getAllAbilities(page, size);

        StepVerifier.create(result)
                .expectNextMatches(actualPage ->
                        Objects.equals(actualPage.getCurrentPage(), expectedPage.getCurrentPage()) &&
                                Objects.equals(actualPage.getPageSize(), expectedPage.getPageSize()) &&
                                Objects.equals(actualPage.getTotalPages(), expectedPage.getTotalPages()) &&
                                actualPage.getItems().equals(expectedPage.getItems())
                )
                .verifyComplete();
    }

    @Test
    void getAbilitiesById_shouldReturnEmptyList_whenNoAbilitiesFound() {
        List<Integer> abilityIds = List.of(1, 2, 3);

        when(abilityRepository.findAllByIdIsIn(abilityIds)).thenReturn(Flux.empty());

        Mono<List<Ability>> result = abilityJpaAdapter.getAbilitiesById(abilityIds);

        StepVerifier.create(result)
                .expectNext(List.of())
                .verifyComplete();
    }

    @Test
    void getAbilitiesById_shouldReturnAbilities_whenAbilitiesExist() {
        List<Integer> abilityIds = List.of(1, 2, 3);
        List<AbilityEntity> abilityEntities = List.of(new AbilityEntity(1,"",""),
                new AbilityEntity(2,"",""),
                new AbilityEntity(3,"",""));
        List<Ability> abilities = List.of(new Ability(1,"","",List.of()),
                new Ability(2,"","",List.of()),
                new Ability(3,"","",List.of()));

        when(abilityRepository.findAllByIdIsIn(abilityIds)).thenReturn(Flux.fromIterable(abilityEntities));
        when(abilityEntityMapper.toDomainList(abilityEntities)).thenReturn(abilities);

        Mono<List<Ability>> result = abilityJpaAdapter.getAbilitiesById(abilityIds);

        StepVerifier.create(result)
                .expectNext(abilities)
                .verifyComplete();
    }
}