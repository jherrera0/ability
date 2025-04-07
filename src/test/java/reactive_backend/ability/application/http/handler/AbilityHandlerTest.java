package reactive_backend.ability.application.http.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactive_backend.ability.application.http.dto.request.CreateAbilityDtoRequest;
import reactive_backend.ability.application.http.dto.request.GetAbilitiesByIdsDtoRequest;
import reactive_backend.ability.application.http.dto.request.ListAbilitiesRequest;
import reactive_backend.ability.application.http.dto.response.AbilityCustomDtoResponse;
import reactive_backend.ability.application.http.dto.response.AbilityDtoResponse;
import reactive_backend.ability.application.http.dto.response.PageResponse;
import reactive_backend.ability.application.http.mapper.IAbilityResponseMapper;
import reactive_backend.ability.application.http.mapper.ICreateAbilityDtoMapper;
import reactive_backend.ability.application.http.mapper.IPageResponseMapper;
import reactive_backend.ability.domain.api.IAbilityServicePort;
import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.model.PageCustom;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class AbilityHandlerTest {

    @Mock
    private IAbilityServicePort abilityServicePort;

    @Mock
    private IPageResponseMapper pageResponseMapper;

    @Mock
    private ICreateAbilityDtoMapper createAbilityDtoMapper;

    @Mock
    private IAbilityResponseMapper abilityResponseMapper;

    @Mock
    private ServerRequest serverRequest;

    @InjectMocks
    private AbilityHandler abilityHandler;

    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAbility_successful() {
        CreateAbilityDtoRequest dtoRequest = new CreateAbilityDtoRequest();
        Ability ability = new Ability();
        AbilityDtoResponse dtoResponse = new AbilityDtoResponse();

        when(serverRequest.bodyToMono(CreateAbilityDtoRequest.class)).thenReturn(Mono.just(dtoRequest));
        when(createAbilityDtoMapper.toAbility(dtoRequest)).thenReturn(ability);
        when(abilityServicePort.createAbility(ability)).thenReturn(Mono.just(ability));
        when(createAbilityDtoMapper.toDtoResponse(ability)).thenReturn(dtoResponse);

        Mono<ServerResponse> response = abilityHandler.createAbility(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void createAbility_emptyRequestBody() {
        when(serverRequest.bodyToMono(CreateAbilityDtoRequest.class)).thenReturn(Mono.empty());

        Mono<ServerResponse> response = abilityHandler.createAbility(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is4xxClientError())
                .verifyComplete();
    }

    @Test
    void createAbility_serviceThrowsException() {
        CreateAbilityDtoRequest dtoRequest = new CreateAbilityDtoRequest();
        Ability ability = new Ability();

        when(serverRequest.bodyToMono(CreateAbilityDtoRequest.class)).thenReturn(Mono.just(dtoRequest));
        when(createAbilityDtoMapper.toAbility(dtoRequest)).thenReturn(ability);
        when(abilityServicePort.createAbility(ability)).thenReturn(Mono.error(new RuntimeException("Service error")));

        Mono<ServerResponse> response = abilityHandler.createAbility(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is4xxClientError())
                .verifyComplete();
    }
    @Test
    void getAllAbilities_shouldReturnBadRequest_whenServiceThrowsException() {
        ListAbilitiesRequest dtoRequest = new ListAbilitiesRequest();

        when(serverRequest.bodyToMono(ListAbilitiesRequest.class)).thenReturn(Mono.just(dtoRequest));
        when(abilityServicePort.getAllAbilities(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(Mono.error(new RuntimeException("Service error")));

        Mono<ServerResponse> response = abilityHandler.getAllAbilities(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is4xxClientError())
                .verifyComplete();
    }

    @Test
    void getAllAbilities_shouldReturnBadRequest_whenRequestBodyIsEmpty() {
        when(serverRequest.bodyToMono(ListAbilitiesRequest.class)).thenReturn(Mono.empty());

        Mono<ServerResponse> response = abilityHandler.getAllAbilities(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is4xxClientError())
                .verifyComplete();
    }

    @Test
    void getAllAbilities_shouldReturnOk_whenRequestIsValid() {
        ListAbilitiesRequest dtoRequest = new ListAbilitiesRequest("asc",
                "name", 0, 10);
        PageCustom<Ability> pageCustom = new PageCustom<>();
        PageResponse<AbilityCustomDtoResponse> pageResponse = new PageResponse<>();

        when(serverRequest.bodyToMono(ListAbilitiesRequest.class)).thenReturn(Mono.just(dtoRequest));
        when(abilityServicePort.getAllAbilities(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(Mono.just(pageCustom));
        when(pageResponseMapper.toPageResponse(pageCustom)).thenReturn(pageResponse);

        Mono<ServerResponse> response = abilityHandler.getAllAbilities(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void getAbilityById_shouldReturnOk_whenRequestIsValid() {
        GetAbilitiesByIdsDtoRequest dtoRequest = new GetAbilitiesByIdsDtoRequest(List.of(1, 2));
        List<AbilityDtoResponse> responseList = List.of(new AbilityDtoResponse(), new AbilityDtoResponse());

        when(serverRequest.bodyToMono(GetAbilitiesByIdsDtoRequest.class)).thenReturn(Mono.just(dtoRequest));
        when(abilityServicePort.getAbilityById(dtoRequest.getIds())).thenReturn(Mono.just(List.of(new Ability(), new Ability())));
        when(abilityResponseMapper.toDtoResponseList(any())).thenReturn(responseList);

        Mono<ServerResponse> response = abilityHandler.getAbilityById(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void getAbilityById_shouldReturnBadRequest_whenRequestBodyIsEmpty() {
        when(serverRequest.bodyToMono(GetAbilitiesByIdsDtoRequest.class)).thenReturn(Mono.empty());

        Mono<ServerResponse> response = abilityHandler.getAbilityById(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is4xxClientError())
                .verifyComplete();
    }

    @Test
    void getAbilityById_shouldReturnBadRequest_whenServiceThrowsException() {
        GetAbilitiesByIdsDtoRequest dtoRequest = new GetAbilitiesByIdsDtoRequest(List.of(1, 2));

        when(serverRequest.bodyToMono(GetAbilitiesByIdsDtoRequest.class)).thenReturn(Mono.just(dtoRequest));
        when(abilityServicePort.getAbilityById(dtoRequest.getIds())).
                thenReturn(Mono.error(new RuntimeException("Service error")));

        Mono<ServerResponse> response = abilityHandler.getAbilityById(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is4xxClientError())
                .verifyComplete();
    }
}