package reactive_backend.ability.domain.model;

import java.util.List;

public class Ability {
    private Integer id;
    private String name;
    private String description;
    private List<Technology> technologies;

    public Ability() {
    }

    public Ability(Integer id, String name, String description, List<Technology> technologies) {
        setId(id);
        setName(name);
        setDescription(description);
        setTechnologies(technologies);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<Technology> technologies) {
        this.technologies = technologies;
    }
    
}
