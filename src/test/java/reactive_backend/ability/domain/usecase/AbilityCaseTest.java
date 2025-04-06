package reactive_backend.ability.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactive_backend.ability.domain.api.IAbilityServicePort;
import reactive_backend.ability.domain.exception.*;
import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.model.PageCustom;
import reactive_backend.ability.domain.model.Technology;
import reactive_backend.ability.domain.spi.IAbilityPersistencePort;
import reactive_backend.ability.domain.spi.ITechnologyClientPort;
import reactive_backend.ability.domain.util.ConstValidation;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
                List.of(new Technology(null,"Tech1",null),
                        new Technology(null,"Tech2",null),
                        new Technology(null,"Tech3",null)));
        when(technologyClientPort.findTechnologiesByNames(any())).thenReturn(Mono.just(List.of(
                new Technology(2,"Tech2","tech2"),
                new Technology(3,"Tech3","tech3"))));


        StepVerifier.create(abilityServicePort.createAbility(ability))
                .expectError(AbilityTechnologyNotFoundException.class)
                .verify();
    }
    @Test
    void getAllAbilitiesWithAscAndName() {
        PageCustom<Ability> pageCustom = new PageCustom<>( 0, 1, 1,
                List.of(new Ability(1, "Ability1", "Description1",
                        List.of(new Technology(1, "Tech1", "tech1")))));
        when(abilityPersistencePort.getAllAbilities(anyInt(), anyInt())).thenReturn(Mono.just(pageCustom));
        when(technologyClientPort.getAllTechnologiesByAbilityId(anyInt()))
                .thenReturn(Mono.just(List.of(new Technology(1, "Tech1", "tech1"))));

        StepVerifier.create(abilityServicePort.getAllAbilities(0, 1, ConstValidation.ASC,
                        ConstValidation.NAME))
                .expectNextMatches(result -> result.getItems().get(0).getName().equals("Ability1"))
                .verifyComplete();
    }
    @Test
    void getAllAbilitiesWithDescAndName() {
        PageCustom<Ability> pageCustom = new PageCustom<>( 0, 1, 1,
                List.of(new Ability(1, "Ability1", "Description1",
                        List.of(new Technology(1, "Tech1", "tech1")))));
        when(abilityPersistencePort.getAllAbilities(anyInt(), anyInt())).thenReturn(Mono.just(pageCustom));
        when(technologyClientPort.getAllTechnologiesByAbilityId(anyInt()))
                .thenReturn(Mono.just(List.of(new Technology(1, "Tech1", "tech1"))));

        StepVerifier.create(abilityServicePort.getAllAbilities(0, 1, ConstValidation.DESC,
                        ConstValidation.NAME))
                .expectNextMatches(result -> result.getItems().get(0).getName().equals("Ability1"))
                .verifyComplete();
    }
    @Test
    void getAllAbilitiesWithAscAndTechSize() {
        PageCustom<Ability> pageCustom = new PageCustom<>( 0, 1, 1,
                List.of(new Ability(1, "Ability1", "Description1",
                        List.of(new Technology(1, "Tech1", "tech1")))));
        when(abilityPersistencePort.getAllAbilities(anyInt(), anyInt())).thenReturn(Mono.just(pageCustom));
        when(technologyClientPort.getAllTechnologiesByAbilityId(anyInt()))
                .thenReturn(Mono.just(List.of(new Technology(1, "Tech1", "tech1"))));

        StepVerifier.create(abilityServicePort.getAllAbilities(0, 1, ConstValidation.ASC,
                        ConstValidation.TECHNOLOGY_SIZE))
                .expectNextMatches(result -> result.getItems().get(0).getName().equals("Ability1"))
                .verifyComplete();
    }
    @Test
    void getAllAbilitiesWithDescAndTechSize() {
        PageCustom<Ability> pageCustom = new PageCustom<>( 0, 1, 1,
                List.of(new Ability(1, "Ability1", "Description1",
                        List.of(new Technology(1, "Tech1", "tech1")))));
        when(abilityPersistencePort.getAllAbilities(anyInt(), anyInt())).thenReturn(Mono.just(pageCustom));
        when(technologyClientPort.getAllTechnologiesByAbilityId(anyInt()))
                .thenReturn(Mono.just(List.of(new Technology(1, "Tech1", "tech1"))));

        StepVerifier.create(abilityServicePort.getAllAbilities(0, 1, ConstValidation.DESC,
                        ConstValidation.TECHNOLOGY_SIZE))
                .expectNextMatches(result -> result.getItems().get(0).getName().equals("Ability1"))
                .verifyComplete();
    }
    @Test
    void getAllAbilitiesWithInvalidPage() {
        when(abilityPersistencePort.getAllAbilities(2, 1))
                .thenReturn(Mono.error(new ListAbilityPageInvalidException()));


        StepVerifier.create(abilityServicePort.getAllAbilities(2, 1, "asc", "name"))
                .expectError(ListAbilityPageInvalidException.class)
                .verify();

    }

    @Test
    void getAllAbilitiesWithInvalidSortField() {
        StepVerifier.create(abilityServicePort.getAllAbilities(0, 1,
                        "asc", "invalidField"))
                .expectError(ListAbilitySortFieldInvalidException.class)
                .verify();
    }

    @Test
    void getAllAbilitiesWithInvalidOrderDirection() {
        StepVerifier.create(abilityServicePort.getAllAbilities(0, 1, "INVALID", "name"))
                .expectError(ListAbilityOrderDirectionInvalidException.class)
                .verify();
    }

    @Test
    void getAllAbilitiesWithEmptyTechnologies() {
        PageCustom<Ability> pageCustom = new PageCustom<>( 0, 1, 1,List.of(new Ability(1, "Ability1", "Description1", List.of())));
        when(abilityPersistencePort.getAllAbilities(anyInt(), anyInt())).thenReturn(Mono.just(pageCustom));
        when(technologyClientPort.getAllTechnologiesByAbilityId(anyInt())).thenReturn(Mono.just(List.of()));

        StepVerifier.create(abilityServicePort.getAllAbilities(0, 1, "asc", "name"))
                .expectError(RuntimeException.class)
                .verify();
    }
    @Test
    void createAbilityWithNullName() {
        Ability ability = new Ability(null, null, "Description1", List.of(new Technology(null, "Tech1", null)));

        StepVerifier.create(abilityServicePort.createAbility(ability))
                .expectError(AbilityNameEmptyException.class)
                .verify();
    }

    @Test
    void createAbilityWithNullDescription() {
        Ability ability = new Ability(null, "Ability1", null, List.of(new Technology(null, "Tech1", null)));

        StepVerifier.create(abilityServicePort.createAbility(ability))
                .expectError(AbilityDescriptionEmptyException.class)
                .verify();
    }

    @Test
    void createAbilityWithTooFewTechnologies() {
        Ability ability = new Ability(null, "Ability1", "Description1", List.of());

        StepVerifier.create(abilityServicePort.createAbility(ability))
                .expectError(AbilityTechnologySizeException.class)
                .verify();
    }

    @Test
    void getAllAbilitiesWithNegativePage() {
        StepVerifier.create(abilityServicePort.getAllAbilities(-1, 1, "asc", "name"))
                .expectError(ListAbilityCurrentPageInvalidException.class)
                .verify();
    }

    @Test
    void getAllAbilitiesWithZeroPageSize() {
        StepVerifier.create(abilityServicePort.getAllAbilities(0, 0, "asc", "name"))
                .expectError(ListAbilityPageSizeInvalidException.class)
                .verify();
    }
}