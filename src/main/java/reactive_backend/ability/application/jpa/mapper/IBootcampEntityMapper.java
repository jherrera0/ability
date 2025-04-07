package reactive_backend.ability.application.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import reactive_backend.ability.application.jpa.entity.BootcampEntity;
import reactive_backend.ability.domain.model.Bootcamp;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBootcampEntityMapper {
    @Mapping(target = "id", ignore = true)
    BootcampEntity toEntity(Bootcamp domain);
    Bootcamp toDomain(BootcampEntity entity);

    List<Bootcamp> toDomainList(List<BootcampEntity> bootcampEntities);
}
