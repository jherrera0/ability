package reactive_backend.ability.domain.exception;

import reactive_backend.ability.domain.util.ConstExceptions;

public class ListAbilityPageInvalidException extends RuntimeException {
    public ListAbilityPageInvalidException() {
        super(ConstExceptions.LIST_ABILITY_PAGE_INVALID);
    }
}
