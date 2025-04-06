package reactive_backend.ability.application.http.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import reactive_backend.ability.application.http.dto.response.AbilityCustomDtoResponse;
import reactive_backend.ability.application.http.dto.response.PageResponse;
import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.model.PageCustom;

@Mapper(componentModel = "spring", uses = {IAbilityCustomResponseMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPageResponseMapper {
    @Mapping(target = "currentPage", source = "page.currentPage")
    @Mapping(target = "pageSize", source = "page.pageSize")
    @Mapping(target = "totalPages", source = "page.totalPages")
    @Mapping(target = "items", source = "page.items")
    PageResponse<AbilityCustomDtoResponse> toPageResponse(PageCustom<Ability> page);
}
