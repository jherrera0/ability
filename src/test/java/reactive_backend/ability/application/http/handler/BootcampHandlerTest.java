package reactive_backend.ability.application.http.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactive_backend.ability.application.http.dto.request.AddBootcampDtoRequest;
import reactive_backend.ability.domain.api.IBootcampServicePort;
import reactive_backend.ability.domain.model.Ability;
import reactive_backend.ability.domain.model.Bootcamp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BootcampHandlerTest {

    @Mock
    private IBootcampServicePort bootcampServicePort;

    @Mock
    private ServerRequest serverRequest;

    @InjectMocks
    private BootcampHandler bootcampHandler;

    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void addBootcamp_shouldReturnOkResponse_whenRequestIsValid() {
        AddBootcampDtoRequest dtoRequest = new AddBootcampDtoRequest(1,
                List.of(new Ability(1,"","",List.of())));
        when(serverRequest.bodyToMono(AddBootcampDtoRequest.class)).thenReturn(Mono.just(dtoRequest));
        when(bootcampServicePort.addBootcamp(any(), any())).thenReturn(Flux.just(new Bootcamp(1,1,1)));

        Mono<ServerResponse> response = bootcampHandler.addBootcamp(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void addBootcamp_shouldReturnBadRequest_whenRequestBodyIsEmpty() {
        when(serverRequest.bodyToMono(AddBootcampDtoRequest.class)).thenReturn(Mono.empty());

        Mono<ServerResponse> response = bootcampHandler.addBootcamp(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is4xxClientError())
                .verifyComplete();
    }

    @Test
    void addBootcamp_shouldReturnBadRequest_whenServiceThrowsException() {
        AddBootcampDtoRequest dtoRequest = new AddBootcampDtoRequest(1,
                List.of(new Ability(1,"","",List.of())));
        when(serverRequest.bodyToMono(AddBootcampDtoRequest.class)).thenReturn(Mono.just(dtoRequest));
        when(bootcampServicePort.addBootcamp(any(), any())).thenReturn(Flux.error(new RuntimeException("Error")));

        Mono<ServerResponse> response = bootcampHandler.addBootcamp(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is4xxClientError())
                .verifyComplete();
    }
}