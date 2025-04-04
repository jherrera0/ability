package reactive_backend.ability.domain.exception;

import reactive_backend.ability.domain.util.ConstExceptions;

public class AbilityTechnologyDuplicateException extends RuntimeException {
    public AbilityTechnologyDuplicateException() {
        super(ConstExceptions.ABILITY_TECHNOLOGY_DUPLICATE_EXCEPTION);
    }
}
