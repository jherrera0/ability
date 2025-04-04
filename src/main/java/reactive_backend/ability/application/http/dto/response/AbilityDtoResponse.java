package reactive_backend.ability.application.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbilityDtoResponse {
    private Integer id;
    private String name;
    private String description;
    private List<TechnologyDtoResponse> technologies;
}
