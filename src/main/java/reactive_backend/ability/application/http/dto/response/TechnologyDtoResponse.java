package reactive_backend.ability.application.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechnologyDtoResponse {
    private Integer id;
    private String name;
    private String description;
}

