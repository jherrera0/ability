package reactive_backend.ability.application.http.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import reactive_backend.ability.application.http.dto.response.TechnologyCustomDtoResponse;
import reactive_backend.ability.domain.model.Technology;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITechnologyCustomResponseMapper {
    TechnologyCustomDtoResponse toDtoResponse(Technology technology);
}
