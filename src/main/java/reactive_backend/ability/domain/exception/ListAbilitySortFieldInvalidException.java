package reactive_backend.ability.domain.exception;

import reactive_backend.ability.domain.util.ConstExceptions;

public class ListAbilitySortFieldInvalidException extends RuntimeException {
    public ListAbilitySortFieldInvalidException() {
        super(ConstExceptions.LIST_ABILITY_SORT_FIELD_INVALID);
    }
}
