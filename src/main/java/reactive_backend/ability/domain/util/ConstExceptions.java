package reactive_backend.ability.domain.util;

public class ConstExceptions {
    public static final String ABILITY_TECHNOLOGY_SIZE_EXCEPTION = "The technology name must be between " +
            ConstValidation.ABILITY_TECHNOLOGY_MIN_SIZE +" and "+ConstValidation.ABILITY_TECHNOLOGY_MAX_SIZE+
            " characters long.";
    public static final String ABILITY_TECHNOLOGY_DUPLICATE_EXCEPTION = "The technology names are duplicate";
    public static final String ABILITY_NAME_EMPTY_EXCEPTION = "The ability name must not be empty";
    public static final String ABILITY_DESCRIPTION_EMPTY_EXCEPTION = "The ability description must not be empty";
    public static final String ABILITY_TECHNOLOGY_NOT_FOUND_EXCEPTION = "The technology name does not exist: ";
    public static final String LIST_ABILITY_ORDER_DIRECTION_INVALID = "The order direction must be 'asc' or 'desc'";
    public static final String LIST_ABILITY_PAGE_SIZE_INVALID = "The page size must be greater than 0";
    public static final String LIST_ABILITY_CURRENT_PAGE_INVALID = "The current page must be greater than 0";
    public static final String LIST_ABILITY_PAGE_INVALID =
            "The page must be greater than 0 and less than or equal to the total number of pages";
    public static final String LIST_ABILITY_SORT_FIELD_INVALID = "The sort field must be 'name' or 'TechnologySize'";


    private ConstExceptions() {
    }
}
