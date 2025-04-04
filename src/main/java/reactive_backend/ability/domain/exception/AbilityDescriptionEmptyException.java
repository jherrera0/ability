package reactive_backend.ability.domain.exception;

import reactive_backend.ability.domain.util.ConstExceptions;

public class AbilityDescriptionEmptyException extends RuntimeException {
    public AbilityDescriptionEmptyException() {
        super(ConstExceptions.ABILITY_DESCRIPTION_EMPTY_EXCEPTION);
    }
}
