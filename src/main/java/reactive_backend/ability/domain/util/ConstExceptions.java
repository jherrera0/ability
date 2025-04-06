package reactive_backend.ability.domain.util;

public class ConstExceptions {
    public static final String ABILITY_TECHNOLOGY_SIZE_EXCEPTION = "The technology name must be between " +
            ConstValidation.ABILITY_TECHNOLOGY_MIN_SIZE +" and "+ConstValidation.ABILITY_TECHNOLOGY_MAX_SIZE+
            " characters long.";
    public static final String ABILITY_TECHNOLOGY_DUPLICATE_EXCEPTION = "The technology names are duplicate";
    public static final String ABILITY_NAME_EMPTY_EXCEPTION = "The ability name must not be empty";
    public static final String ABILITY_DESCRIPTION_EMPTY_EXCEPTION = "The ability description must not be empty";
    public static final String ABILITY_TECHNOLOGY_NOT_FOUND_EXCEPTION = "The technology name does not exist: ";


    private ConstExceptions() {
    }
}
