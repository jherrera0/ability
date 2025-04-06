package reactive_backend.ability.application.http.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactive_backend.ability.application.http.dto.request.CreateAbilityDtoRequest;
import reactive_backend.ability.application.http.dto.response.AbilityDtoResponse;
import reactive_backend.ability.application.http.mapper.ICreateAbilityDtoMapper;
import reactive_backend.ability.domain.api.IAbilityServicePort;
import reactive_backend.ability.domain.model.Ability;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class AbilityHandlerTest {

    @Mock
    private IAbilityServicePort abilityServicePort;

    @Mock
    private ICreateAbilityDtoMapper createAbilityDtoMapper;

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
}