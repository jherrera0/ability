package reactive_backend.ability.application.http.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListAbilitiesRequest {
    private String sortField;
    private String sortOrder;
    private Integer currentPage;
    private Integer pageSize;
}
