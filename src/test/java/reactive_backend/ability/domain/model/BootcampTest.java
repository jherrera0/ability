package reactive_backend.ability.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BootcampTest {

    @Test
    void constructorShouldInitializeFields() {
        Bootcamp bootcamp = new Bootcamp(1, 2, 3);
        assertEquals(1, bootcamp.getId());
        assertEquals(2, bootcamp.getBootcampId());
        assertEquals(3, bootcamp.getAbilityId());
    }

    @Test
    void defaultConstructorShouldInitializeFieldsToNull() {
        Bootcamp bootcamp = new Bootcamp();
        assertNull(bootcamp.getId());
        assertNull(bootcamp.getBootcampId());
        assertNull(bootcamp.getAbilityId());
    }

    @Test
    void setIdShouldUpdateId() {
        Bootcamp bootcamp = new Bootcamp();
        bootcamp.setId(1);
        assertEquals(1, bootcamp.getId());
    }

    @Test
    void setBootcampIdShouldUpdateBootcampId() {
        Bootcamp bootcamp = new Bootcamp();
        bootcamp.setBootcampId(2);
        assertEquals(2, bootcamp.getBootcampId());
    }

    @Test
    void setAbilityIdShouldUpdateAbilityId() {
        Bootcamp bootcamp = new Bootcamp();
        bootcamp.setAbilityId(3);
        assertEquals(3, bootcamp.getAbilityId());
    }
}