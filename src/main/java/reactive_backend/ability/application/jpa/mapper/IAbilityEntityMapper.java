package reactive_backend.ability.application.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import reactive_backend.ability.application.jpa.entity.AbilityEntity;
import reactive_backend.ability.domain.model.Ability;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IAbilityEntityMapper {
    Ability toDomain(AbilityEntity abilityEntity);

    @Mapping(target = "id", ignore = true)
    AbilityEntity toEntity(Ability ability);

    List<Ability> toDomainList(List<AbilityEntity> abilityEntities);
}
