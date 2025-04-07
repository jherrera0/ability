package reactive_backend.ability.domain.model;

public class Bootcamp {
    private Integer id;
    private Integer bootcampId;
    private Integer abilityId;

    public Bootcamp() {
    }

    public Bootcamp(Integer id, Integer bootcampId, Integer abilityId) {
        this.id = id;
        this.bootcampId = bootcampId;
        this.abilityId = abilityId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBootcampId() {
        return bootcampId;
    }

    public void setBootcampId(Integer bootcampId) {
        this.bootcampId = bootcampId;
    }

    public Integer getAbilityId() {
        return abilityId;
    }

    public void setAbilityId(Integer abilityId) {
        this.abilityId = abilityId;
    }
}
