package reactive_backend.ability.domain.usecase;

import reactive_backend.ability.domain.api.IAbilityServicePort;
import reactive_backend.ability.domain.exception.*;
import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.model.Technology;
import reactive_backend.ability.domain.spi.IAbilityPersistencePort;
import reactive_backend.ability.domain.spi.ITechnologyClientPort;
import reactive_backend.ability.domain.util.ConstValidation;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AbilityCase implements IAbilityServicePort {
    private final IAbilityPersistencePort abilityPersistencePort;
    private final ITechnologyClientPort technologyClientPort;

    public AbilityCase(IAbilityPersistencePort abilityPersistencePort,
                       ITechnologyClientPort technologyClientPort) {
        this.abilityPersistencePort = abilityPersistencePort;
        this.technologyClientPort = technologyClientPort;
    }

    @Override
    public Mono<Ability> createAbility(Ability ability) {
        Mono<Ability> error = validateParams(ability);
        if (error != null) return error;
        List<String> names = ability.getTechnologies().stream()
                .map(Technology::getName)
                .toList();

        if (hasDuplicates(names)) {
            return Mono.error(new AbilityTechnologyDuplicateException());
        }

        return technologyClientPort.findTechnologiesByNames(names)
                .flatMap(technologies -> {
                    Set<String> foundNames = technologies.stream()
                            .map(Technology::getName)
                            .collect(Collectors.toSet());

                    List<String> notFound = names.stream()
                            .filter(name -> !foundNames.contains(name))
                            .toList();

                    if (!notFound.isEmpty()) {
                        return Mono.error(new AbilityTechnologyNotFoundException(notFound));
                    }

                    // Guardar el Ability
                    return abilityPersistencePort.saveAbility(ability)
                            .flatMap(savedAbility ->
                                    technologyClientPort.linkTechnologiesToAbility(savedAbility.getId(), technologies)
                                    .then(Mono.fromCallable(() -> {
                                        savedAbility.setTechnologies(technologies);
                                        return savedAbility;
                                    })));
                });
    }

    private static Mono<Ability> validateParams(Ability ability) {
        if(ability.getName() == null || ability.getName().isEmpty()) {
            return Mono.error(new AbilityNameEmptyException());
        }

        if (ability.getDescription() == null || ability.getDescription().isEmpty()) {
            return Mono.error(new AbilityDescriptionEmptyException());
        }

        if( ability.getTechnologies().size() < ConstValidation.ABILITY_TECHNOLOGY_MIN_SIZE ||
                ability.getTechnologies().size() > ConstValidation.ABILITY_TECHNOLOGY_MAX_SIZE) {
            return Mono.error(new AbilityTechnologySizeException());
        }
        return null;
    }


    public boolean hasDuplicates(List<String> names) {
        Set<String> uniqueNames = new HashSet<>();
        for (String name : names) {
            if (!uniqueNames.add(name)) {
                return true; // Ya existía, entonces es duplicado
            }
        }
        return false; // Todos eran únicos
    }
}
