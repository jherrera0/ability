package reactive_backend.ability.domain.exception;

import reactive_backend.ability.domain.util.ConstExceptions;

import java.util.List;

public class AbilityTechnologyNotFoundException extends RuntimeException {
    public AbilityTechnologyNotFoundException(List<String> notFound) {
        super(ConstExceptions.ABILITY_TECHNOLOGY_NOT_FOUND_EXCEPTION + notFound);
    }
}
