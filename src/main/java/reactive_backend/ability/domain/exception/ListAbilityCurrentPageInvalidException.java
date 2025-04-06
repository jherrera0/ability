package reactive_backend.ability.domain.exception;

import reactive_backend.ability.domain.util.ConstExceptions;

public class ListAbilityCurrentPageInvalidException extends RuntimeException {
    public ListAbilityCurrentPageInvalidException() {
        super(ConstExceptions.LIST_ABILITY_CURRENT_PAGE_INVALID);
    }
}
