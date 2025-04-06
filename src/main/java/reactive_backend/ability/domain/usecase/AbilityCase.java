package reactive_backend.ability.domain.usecase;

import reactive_backend.ability.domain.api.IAbilityServicePort;
import reactive_backend.ability.domain.exception.*;
import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.model.PageCustom;
import reactive_backend.ability.domain.model.Technology;
import reactive_backend.ability.domain.spi.IAbilityPersistencePort;
import reactive_backend.ability.domain.spi.ITechnologyClientPort;
import reactive_backend.ability.domain.util.ConstValidation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AbilityCase implements IAbilityServicePort {
    private final IAbilityPersistencePort abilityPersistencePort;
    private final ITechnologyClientPort technologyClientPort;
    static Logger logger = Logger.getLogger(AbilityCase.class.getName());
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

    @Override
    public Mono<PageCustom<Ability>> getAllAbilities(Integer page, Integer size, String sortDirection, String sortField) {
        Mono<PageCustom<Ability>> error = validateParameters(sortDirection, size, page, sortField);
        if (error != null) return error;

        return abilityPersistencePort.getAllAbilities(page, size)
                .flatMap(pageCustom -> {
                    if (pageCustom.getTotalPages() < pageCustom.getCurrentPage() + ConstValidation.ONE) {
                        return Mono.error(new ListAbilityPageInvalidException());
                    }

                    // Convertimos la lista a Flux para poder mapear asincrónicamente
                    return Flux.fromIterable(pageCustom.getItems())
                            .flatMapSequential(ability ->
                                    technologyClientPort.getAllTechnologiesByAbilityId(ability.getId())
                                            .flatMap(technologies -> {
                                                if (technologies.isEmpty()) {
                                                    return Mono.error(new RuntimeException("No se encontraron tecnologías para la habilidad con ID: " + ability.getId()));
                                                }
                                                ability.setTechnologies(technologies);
                                                return Mono.just(ability);
                                            })
                            )
                            .collectList()
                            .flatMap(updatedAbilities -> {
                                pageCustom.setItems(updatedAbilities);

                                if (sortField.equals(ConstValidation.TECHNOLOGY_SIZE) || sortField.equals(ConstValidation.NAME)) {
                                    return sortByField(updatedAbilities, sortField, sortDirection)
                                            .flatMap(sorted -> {
                                                pageCustom.setItems(sorted);
                                                return Mono.just(pageCustom);
                                            });
                                }

                                return Mono.just(pageCustom);
                            });
                });
    }


    private static Mono<List<Ability>> sortByField(List<Ability> abilities, String sortField,
                                                   String orderDirection) {
        if (sortField.compareTo(ConstValidation.NAME) == ConstValidation.ZERO) {
            if (orderDirection.compareTo(ConstValidation.ASC) == ConstValidation.ZERO) {
                return Mono.just(abilities.stream()
                        .sorted(Comparator.comparing(Ability::getName))
                        .toList());
            } else {
                return Mono.just(abilities.stream()
                        .sorted((a1, a2) -> a2.getName().compareTo(a1.getName()))
                        .toList());
            }
        } else {
            if (orderDirection.compareTo(ConstValidation.ASC) == ConstValidation.ZERO) {
                return Mono.just(abilities.stream()
                        .sorted(Comparator.comparingInt(a -> a.getTechnologies().size()))
                        .toList());
            } else {
                return Mono.just(abilities.stream()
                        .sorted((a1, a2) -> Integer.compare(a2.getTechnologies().size(), a1.getTechnologies().size()))
                        .toList());
            }
        }
    }

    private static Mono<PageCustom<Ability>> validateParameters(String orderDirection, Integer pageSize,
                                                                Integer currentPage, String sortField) {

        if(orderDirection.compareTo(ConstValidation.ASC) != ConstValidation.ZERO &&
                orderDirection.compareTo(ConstValidation.DESC) != ConstValidation.ZERO) {
            return Mono.error(new ListAbilityOrderDirectionInvalidException());
        }

        if(sortField.compareTo(ConstValidation.NAME) != ConstValidation.ZERO &&
                sortField.compareTo(ConstValidation.TECHNOLOGY_SIZE) != ConstValidation.ZERO) {
            return Mono.error(new ListAbilitySortFieldInvalidException());
        }

        if (pageSize <= ConstValidation.ZERO) {
            return Mono.error(new ListAbilityPageSizeInvalidException());
        }

        if(currentPage < ConstValidation.ZERO) {
            return Mono.error(new ListAbilityCurrentPageInvalidException());
        }
        return null;
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
