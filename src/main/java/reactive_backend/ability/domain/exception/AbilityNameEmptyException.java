package reactive_backend.ability.domain.exception;

import reactive_backend.ability.domain.util.ConstExceptions;

public class AbilityNameEmptyException extends RuntimeException {
    public AbilityNameEmptyException() {
        super(ConstExceptions.ABILITY_NAME_EMPTY_EXCEPTION);
    }
}
