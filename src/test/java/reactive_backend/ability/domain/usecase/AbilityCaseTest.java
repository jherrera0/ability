package reactive_backend.ability.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactive_backend.ability.domain.api.IAbilityServicePort;
import reactive_backend.ability.domain.exception.*;
import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.model.Technology;
import reactive_backend.ability.domain.spi.IAbilityPersistencePort;
import reactive_backend.ability.domain.spi.ITechnologyClientPort;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AbilityCaseTest {

    private IAbilityPersistencePort abilityPersistencePort;
    private ITechnologyClientPort technologyClientPort;
    private IAbilityServicePort abilityServicePort;

    @BeforeEach
    void setUp() {
        abilityPersistencePort = Mockito.mock(IAbilityPersistencePort.class);
        technologyClientPort = Mockito.mock(ITechnologyClientPort.class);
        abilityServicePort = new AbilityCase(abilityPersistencePort, technologyClientPort);
    }

    @Test
    void createAbilitySuccessfully() {
        Ability ability = new Ability(null,"Ability1", "Description1",
                List.of(new Technology(null,"Tech1",null),
                        new Technology(null,"Tech2",null),
                        new Technology(null,"Tech3",null)));
        when(technologyClientPort.findTechnologiesByNames(any())).thenReturn(Mono.just(List.of(
                new Technology(1,"Tech1","tech1"),
                new Technology(2,"Tech2","tech2"),
                new Technology(3,"Tech3","tech3"))));
        when(technologyClientPort.linkTechnologiesToAbility(any(), any())).thenReturn(Mono.empty());
        when(abilityPersistencePort.saveAbility(any())).thenReturn(Mono.just(ability));
        when(technologyClientPort.linkTechnologiesToAbility(any(), any())).thenReturn(Mono.empty());

        StepVerifier.create(abilityServicePort.createAbility(ability))
                .expectNextMatches(savedAbility -> savedAbility.getName().equals("Ability1"))
                .verifyComplete();
    }

    @Test
    void createAbilityWithEmptyName() {
        Ability ability = new Ability(null,"", "Description1",
                List.of(new Technology(null,"Tech1", null)));

        StepVerifier.create(abilityServicePort.createAbility(ability))
                .expectError(AbilityNameEmptyException.class)
                .verify();
    }

    @Test
    void createAbilityWithEmptyDescription() {
        Ability ability = new Ability(null,"Ability1", "",List.of(new Technology(null,"Tech1", null)));

        StepVerifier.create(abilityServicePort.createAbility(ability))
                .expectError(AbilityDescriptionEmptyException.class)
                .verify();
    }

    @Test
    void createAbilityWithDuplicateTechnologies() {
        Ability ability = new Ability(null,"Ability1", "Description1",
                List.of(new Technology(null,"Tech1",null),
                        new Technology(null,"Tech1",null),
                        new Technology(null,"Tech2",null)));

        StepVerifier.create(abilityServicePort.createAbility(ability))
                .expectError(AbilityTechnologyDuplicateException.class)
                .verify();
    }

    @Test
    void createAbilityWithInvalidTechnologySize() {
        Ability ability = new Ability(null,"Ability1", "Description1", List.of(new Technology()));

        StepVerifier.create(abilityServicePort.createAbility(ability))
                .expectError(AbilityTechnologySizeException.class)
                .verify();
    }

    @Test
    void createAbilityWithNonExistentTechnologies() {
        Ability ability = new Ability(null,"Ability1", "Description1",
                List.of(new Technology(null,"Tech1",null),new Technology(null,"Tech2",null),
                        new Technology(null,"Tech3",null)));
        when(technologyClientPort.findTechnologiesByNames(any())).thenReturn(Mono.just(List.of(
                new Technology(2,"Tech2","tech2"),
                new Technology(3,"Tech3","tech3"))));


        StepVerifier.create(abilityServicePort.createAbility(ability))
                .expectError(AbilityTechnologyNotFoundException.class)
                .verify();
    }
}