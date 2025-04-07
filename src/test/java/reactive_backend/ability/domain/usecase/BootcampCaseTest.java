package reactive_backend.ability.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactive_backend.ability.domain.model.Technology;
import reactive_backend.ability.domain.spi.ITechnologyClientPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.model.Bootcamp;
import reactive_backend.ability.domain.spi.IAbilityPersistencePort;
import reactive_backend.ability.domain.spi.IBootcampPersistencePort;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class BootcampCaseTest {

    @Mock
    private IBootcampPersistencePort bootcampPersistencePort;

    @Mock
    private IAbilityPersistencePort abilityPersistencePort;

    @Mock
    private ITechnologyClientPort technologyClientPort;

    @InjectMocks
    private BootcampCase bootcampCase;

    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void addBootcampShouldReturnFluxOfBootcamps() {
        Bootcamp bootcamp = new Bootcamp();
        when(bootcampPersistencePort.addBootcamp(any(Integer.class), any(List.class)))
                .thenReturn(Flux.just(bootcamp));

        StepVerifier.create(bootcampCase.addBootcamp(1,
                        List.of(new Ability(), new Ability(), new Ability())))
                .expectNext(bootcamp)
                .verifyComplete();
    }


    @Test
    void addBootcampShouldReturnEmptyFluxWhenNoAbilitiesProvided() {
        when(bootcampPersistencePort.addBootcamp(any(Integer.class), any(List.class)))
                .thenReturn(Flux.empty());

        StepVerifier.create(bootcampCase.addBootcamp(1, List.of()))
                .verifyComplete();
    }

    @Test
    void getAllAbilitiesByBootcampIdShouldReturnListOfAbilities() {
        // Arrange
        List<Integer> abilityIds = List.of(1, 2, 3);

        // Crear objetos Ability de prueba
        Ability ability1 = new Ability();
        ability1.setId(1);
        Ability ability2 = new Ability();
        ability2.setId(2);
        Ability ability3 = new Ability();
        ability3.setId(3);

        List<Ability> abilities = List.of(ability1, ability2, ability3);

        // Mockear bootcampPersistencePort
        when(bootcampPersistencePort.getAllAbilitiesByBootcampId(any(Integer.class)))
                .thenReturn(Mono.just(abilityIds));

        // Mockear abilityPersistencePort
        when(abilityPersistencePort.getAbilitiesById(anyList()))
                .thenReturn(Mono.just(abilities));

        // Mockear technologyClientPort por cada id de habilidad
        when(technologyClientPort.getAllTechnologiesByAbilityId(any(Integer.class)))
                .thenAnswer(invocation -> {
                    Integer abilityId = invocation.getArgument(0);
                    Technology tech1 = new Technology(); tech1.setName("Tech A " + abilityId);
                    Technology tech2 = new Technology(); tech2.setName("Tech B " + abilityId);
                    return Mono.just(List.of(tech1, tech2));
                });

        // Act + Assert
        StepVerifier.create(bootcampCase.getAllAbilitiesByBootcampId(1))
                .assertNext(result -> {
                    assertThat(result).hasSize(3);
                    result.forEach(ability -> {
                        assertThat(ability.getId()).isNotNull();
                        assertThat(ability.getTechnologies()).hasSize(2); // Verifica que las tecnologías hayan sido asignadas
                    });
                })
                .verifyComplete();
    }


    @Test
    void getAllAbilitiesByBootcampIdShouldReturnEmptyListWhenNoAbilitiesFound() {
        when(bootcampPersistencePort.getAllAbilitiesByBootcampId(any(Integer.class)))
                .thenReturn(Mono.just(List.of()));
        when(abilityPersistencePort.getAbilitiesById(any(List.class)))
                .thenReturn(Mono.just(List.of()));

        StepVerifier.create(bootcampCase.getAllAbilitiesByBootcampId(1))
                .expectNext(List.of())
                .verifyComplete();
    }
}