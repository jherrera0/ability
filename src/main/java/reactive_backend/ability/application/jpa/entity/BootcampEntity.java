package reactive_backend.ability.application.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("bootcamp_abilities")
public class BootcampEntity {
    @Id
    private Integer id;

    @Column("bootcamp_Id")
    private Integer bootcampId;

    @Column("ability_Id")
    private Integer abilityId;
}
