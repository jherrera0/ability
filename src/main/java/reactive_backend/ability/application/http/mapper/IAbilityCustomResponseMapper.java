package reactive_backend.ability.application.http.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import reactive_backend.ability.application.http.dto.response.AbilityCustomDtoResponse;
import reactive_backend.ability.domain.model.Ability;

@Mapper(componentModel = "spring",
        uses = {ITechnologyCustomResponseMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IAbilityCustomResponseMapper {
    @Mapping(target = "technologies", source = "technologies")
    AbilityCustomDtoResponse toDtoResponse(Ability ability);
}
