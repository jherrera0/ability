package reactive_backend.ability.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TechnologyTest {

    @Test
    void technologyConstructorSetsAllFields() {
        Technology technology = new Technology(1, "Java", "Programming language");

        assertEquals(1, technology.getId());
        assertEquals("Java", technology.getName());
        assertEquals("Programming language", technology.getDescription());
    }

    @Test
    void technologyDefaultConstructor() {
        Technology technology = new Technology();

        assertNull(technology.getId());
        assertNull(technology.getName());
        assertNull(technology.getDescription());
    }

    @Test
    void setIdUpdatesId() {
        Technology technology = new Technology();
        technology.setId(2);

        assertEquals(2, technology.getId());
    }

    @Test
    void setNameUpdatesName() {
        Technology technology = new Technology();
        technology.setName("Spring Boot");

        assertEquals("Spring Boot", technology.getName());
    }

    @Test
    void setDescriptionUpdatesDescription() {
        Technology technology = new Technology();
        technology.setDescription("Framework");

        assertEquals("Framework", technology.getDescription());
    }
}