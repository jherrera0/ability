package reactive_backend.ability.domain.exception;

import reactive_backend.ability.domain.util.ConstExceptions;

public class ListAbilityPageSizeInvalidException extends RuntimeException {
    public ListAbilityPageSizeInvalidException() {
        super(ConstExceptions.LIST_ABILITY_PAGE_SIZE_INVALID);
    }
}
