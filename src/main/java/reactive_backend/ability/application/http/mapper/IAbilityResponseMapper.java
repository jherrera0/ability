package reactive_backend.ability.application.http.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import reactive_backend.ability.application.http.dto.response.AbilityDtoResponse;
import reactive_backend.ability.domain.model.Ability;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {ITechnologyDtoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IAbilityResponseMapper {
    @Mapping(target = "technologies", source = "technologies")
    AbilityDtoResponse toDtoResponse(Ability ability);

    List<AbilityDtoResponse> toDtoResponseList(List<Ability> abilities);
}
