package reactive_backend.ability.domain.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AbilityTest {

    @Test
    void abilityConstructorSetsAllFields() {
        List<Technology> technologies = List.of(new Technology(null, "Java", null),
                new Technology(null,"Spring Boot",null));
        Ability ability = new Ability(1, "Coding", "Ability to code", technologies);

        assertEquals(1, ability.getId());
        assertEquals("Coding", ability.getName());
        assertEquals("Ability to code", ability.getDescription());
        assertEquals(technologies, ability.getTechnologies());
    }

    @Test
    void abilityDefaultConstructor() {
        Ability ability = new Ability();

        assertNull(ability.getId());
        assertNull(ability.getName());
        assertNull(ability.getDescription());
        assertNull(ability.getTechnologies());
    }

    @Test
    void setIdUpdatesId() {
        Ability ability = new Ability();
        ability.setId(2);

        assertEquals(2, ability.getId());
    }

    @Test
    void setNameUpdatesName() {
        Ability ability = new Ability();
        ability.setName("Testing");

        assertEquals("Testing", ability.getName());
    }

    @Test
    void setDescriptionUpdatesDescription() {
        Ability ability = new Ability();
        ability.setDescription("Testing description");

        assertEquals("Testing description", ability.getDescription());
    }

    @Test
    void setTechnologiesUpdatesTechnologies() {
        List<Technology> technologies = List.of(new Technology(null,"JUnit",null));
        Ability ability = new Ability();
        ability.setTechnologies(technologies);

        assertEquals(technologies, ability.getTechnologies());
    }
}