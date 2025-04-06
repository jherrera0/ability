package reactive_backend.ability.application.http.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactive_backend.ability.application.http.dto.request.CreateAbilityDtoRequest;
import reactive_backend.ability.application.http.dto.response.AbilityDtoResponse;
import reactive_backend.ability.application.http.dto.response.TechnologyDtoResponse;
import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.model.Technology;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ICreateAbilityDtoMapperTest {

    @Mock
    private ITechnologyDtoMapper technologyDtoMapper;

    @InjectMocks
    private final ICreateAbilityDtoMapper mapper = Mappers.getMapper(ICreateAbilityDtoMapper.class);

    @Test
    void toAbility_shouldMapFieldsCorrectly() {
        CreateAbilityDtoRequest request = new CreateAbilityDtoRequest();
        request.setTechnologiesNames(List.of("Java", "Spring"));

        Ability ability = mapper.toAbility(request);

        assertTrue(ability.getTechnologies().stream().anyMatch(tech -> "Java".equals(tech.getName())));
        assertTrue(ability.getTechnologies().stream().anyMatch(tech -> "Spring".equals(tech.getName())));
    }

    @Test
    void toAbility_shouldIgnoreIdField() {
        CreateAbilityDtoRequest request = new CreateAbilityDtoRequest();
        request.setTechnologiesNames(List.of("Java"));

        Ability ability = mapper.toAbility(request);

        assertNull(ability.getId());
    }

    @Test
    void toDtoResponse_shouldMapFieldsCorrectly() {
        Ability ability = new Ability();
        ability.setId(1);
        List<Technology> technologies = List.of(new Technology(null, "Java", null));
        ability.setTechnologies(technologies);

        // Mock the behavior of technologyDtoMapper.toDtoResponse
        when(technologyDtoMapper.toDtoResponse(technologies.get(0)))
                .thenReturn(new TechnologyDtoResponse(null,"Java",null));

        AbilityDtoResponse response = mapper.toDtoResponse(ability);

        assertEquals(1, response.getId());
        assertTrue(response.getTechnologies().contains(new TechnologyDtoResponse(null,"Java",null)));
    }

    @Test
    void map_shouldReturnEmptyListWhenNamesIsNull() {
        List<Technology> technologies = mapper.map(null);

        assertTrue(technologies.isEmpty());
    }

    @Test
    void map_shouldMapNamesToTechnologies() {
        List<Technology> technologies = mapper.map(List.of("Java", "Spring"));

        assertTrue(technologies.stream().anyMatch(tech -> "Java".equals(tech.getName())));
        assertTrue(technologies.stream().anyMatch(tech -> "Spring".equals(tech.getName())));
    }
}