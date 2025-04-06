package reactive_backend.ability.application.http.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import reactive_backend.ability.application.http.dto.request.CreateAbilityDtoRequest;
import reactive_backend.ability.application.http.dto.response.AbilityDtoResponse;
import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.model.Technology;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring",
        uses = {ITechnologyDtoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICreateAbilityDtoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "technologies", source = "technologiesNames")
    Ability toAbility(CreateAbilityDtoRequest createAbilityDto);

    @Mapping(target = "technologies", source = "technologies")
    AbilityDtoResponse toDtoResponse(Ability ability);

    default List<Technology> map(List<String> names) {
        if (names == null) {
            return Collections.emptyList();
        }
        return names.stream()
                .map(name -> {
                    Technology tech = new Technology();
                    tech.setName(name);
                    return tech;
                })
                .toList();
    }
}
