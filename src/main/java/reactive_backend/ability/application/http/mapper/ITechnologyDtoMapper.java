package reactive_backend.ability.application.http.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import reactive_backend.ability.application.http.dto.response.TechnologyDtoResponse;
import reactive_backend.ability.domain.model.Technology;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITechnologyDtoMapper {


    @Mapping(target = "id", source = "technology.id")
    @Mapping(target = "name", source = "technology.name")
    @Mapping(target = "description", source = "technology.description")
    TechnologyDtoResponse toDtoResponse(Technology technology);
}
