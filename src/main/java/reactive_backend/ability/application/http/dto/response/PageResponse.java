package reactive_backend.ability.application.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalPages;
    private List<T> items;
}
