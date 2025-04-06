package reactive_backend.ability.domain.exception;

import reactive_backend.ability.domain.util.ConstExceptions;

public class AbilityTechnologySizeException extends RuntimeException {
    public AbilityTechnologySizeException() {
        super(ConstExceptions.ABILITY_TECHNOLOGY_SIZE_EXCEPTION);
    }
}
