package reactive_backend.ability.domain.exception;

import reactive_backend.ability.domain.util.ConstExceptions;

public class ListAbilityOrderDirectionInvalidException extends RuntimeException {
    public ListAbilityOrderDirectionInvalidException() {
        super(ConstExceptions.LIST_ABILITY_ORDER_DIRECTION_INVALID);
    }
}
