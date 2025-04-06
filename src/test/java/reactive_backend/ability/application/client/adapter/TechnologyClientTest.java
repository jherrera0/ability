package reactive_backend.ability.application.client.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.*;
import reactive_backend.ability.domain.model.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TechnologyClientTest {

    private WebClient webClient;
    private TechnologyClient technologyClient;
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    private WebClient.RequestBodySpec requestBodySpec;
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        webClient = mock(WebClient.class);
        requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        requestBodySpec = mock(WebClient.RequestBodySpec.class);
        requestHeadersSpec = (WebClient.RequestHeadersSpec<?>) mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        // Simula la cadena de construcción de WebClient
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        // Instanciar el objeto bajo prueba con el WebClient mockeado
        technologyClient = new TechnologyClient(webClientBuilder);
    }


    @Test
    void findTechnologiesByNames_shouldReturnTechnologies() {
        List<String> names = List.of("Java", "Spring");
        List<Technology> technologies = List.of(new Technology(1,"Java","Java"),
                new Technology(2,"Spring","Spring"));

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/technology/getByName")).thenReturn(requestBodySpec);
        doReturn(requestHeadersSpec).when(requestBodySpec).bodyValue(any());
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(Technology.class)).thenReturn(Flux.fromIterable(technologies));

        StepVerifier.create(technologyClient.findTechnologiesByNames(names))
                .expectNext(technologies)
                .verifyComplete();
    }

    @Test
    void linkTechnologiesToAbility_shouldReturnVoid() {
        Integer abilityId = 1;
        List<Technology> technologies = List.of(new Technology(1,"React","React"),
                new Technology(2,"Angular","Angular"));

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/technology/addAbility")).thenReturn(requestBodySpec);
        doReturn(requestHeadersSpec).when(requestBodySpec).bodyValue(any());
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        StepVerifier.create(technologyClient.linkTechnologiesToAbility(abilityId, technologies))
                .verifyComplete();
    }
}
