package reactive_backend.ability.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.model.Bootcamp;
import reactive_backend.ability.domain.spi.IAbilityPersistencePort;
import reactive_backend.ability.domain.spi.IBootcampPersistencePort;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BootcampCaseTest {

    @Mock
    private IBootcampPersistencePort bootcampPersistencePort;

    @Mock
    private IAbilityPersistencePort abilityPersistencePort;

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
    void getAllAbilitiesByBootcampIdShouldReturnListOfAbilities() {
        List<Integer> abilityIds = List.of(1, 2, 3);
        List<Ability> abilities = List.of(new Ability(1,"","",List.of()),
                new Ability(2,"","",List.of()),
                new Ability(3,"","",List.of()));
        when(bootcampPersistencePort.getAllAbilitiesByBootcampId(any(Integer.class)))
                .thenReturn(Mono.just(abilityIds));
        when(abilityPersistencePort.getAbilitiesById(any(List.class)))
                .thenReturn(Mono.just(abilities));

        StepVerifier.create(bootcampCase.getAllAbilitiesByBootcampId(1))
                .expectNext(abilities)
                .verifyComplete();
    }

    @Test
    void addBootcampShouldReturnEmptyFluxWhenNoAbilitiesProvided() {
        when(bootcampPersistencePort.addBootcamp(any(Integer.class), any(List.class)))
                .thenReturn(Flux.empty());

        StepVerifier.create(bootcampCase.addBootcamp(1, List.of()))
                .verifyComplete();
    }
}