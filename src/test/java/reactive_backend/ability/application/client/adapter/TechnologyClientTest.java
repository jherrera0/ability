package reactive_backend.ability.application.client.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.reactive.function.client.WebClient;
import reactive_backend.ability.application.http.dto.request.AddAbilityDtoRequest;
import reactive_backend.ability.application.http.dto.request.GetByNameRequest;
import reactive_backend.ability.domain.model.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TechnologyClientTest {

    private WebClient webClientMock;
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;
    private WebClient.RequestBodyUriSpec requestBodyUriSpecMock;
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;
    private WebClient.RequestBodySpec requestBodySpecMock;
    private WebClient.ResponseSpec responseSpecMock;
    private TechnologyClient technologyClient;

    @BeforeEach
    void setUp() {
        // Mocks
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        webClientMock = mock(WebClient.class);
        requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        responseSpecMock = mock(WebClient.ResponseSpec.class);
        requestBodyUriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        requestBodySpecMock = mock(WebClient.RequestBodySpec.class);

        // Configure mock chain
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClientMock);
        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);

        // Inject client with mocked builder
        technologyClient = new TechnologyClient(webClientBuilder);
    }
    @Test
    void findTechnologiesByNames_shouldCallCorrectEndpointWithCorrectBody() {
        // Arrange
        List<String> techNames = Arrays.asList("Java", "Spring");
        List<Technology> expectedTechnologies = Arrays.asList(
                new Technology(1, "Java","Java"),
                new Technology(2, "Spring","Spring")
        );

        ArgumentCaptor<GetByNameRequest> requestCaptor = ArgumentCaptor.forClass(GetByNameRequest.class);

        when(webClientMock.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri("/technology/getByName")).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.bodyValue(requestCaptor.capture())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToFlux(Technology.class)).thenReturn(Flux.fromIterable(expectedTechnologies));

        // Act
        Mono<List<Technology>> result = technologyClient.findTechnologiesByNames(techNames);

        // Assert
        StepVerifier.create(result)
                .expectNext(expectedTechnologies)
                .verifyComplete();

        GetByNameRequest capturedRequest = requestCaptor.getValue();
        assertEquals(techNames, capturedRequest.getNames());

        verify(webClientMock).post();
        verify(requestBodyUriSpecMock).uri("/technology/getByName");
        verify(requestBodySpecMock).bodyValue(any(GetByNameRequest.class));
        verify(requestHeadersSpecMock).retrieve();
        verify(responseSpecMock).bodyToFlux(Technology.class);
    }

    @Test
    void linkTechnologiesToAbility_shouldCallCorrectEndpointWithCorrectBody() {
        // Arrange
        Integer abilityId = 1;
        List<Technology> technologies = Arrays.asList(
                new Technology(1, "Java", "Java"),
                new Technology(2, "Spring", "Spring")
        );

        ArgumentCaptor<AddAbilityDtoRequest> requestCaptor = ArgumentCaptor.forClass(AddAbilityDtoRequest.class);

        when(webClientMock.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri("/technology/addAbility")).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.bodyValue(requestCaptor.capture())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = technologyClient.linkTechnologiesToAbility(abilityId, technologies);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        AddAbilityDtoRequest capturedRequest = requestCaptor.getValue();
        assertEquals(abilityId, capturedRequest.getAbilityId());
        assertSame(technologies, capturedRequest.getTechnologies());

        verify(webClientMock).post();
        verify(requestBodyUriSpecMock).uri("/technology/addAbility");
        verify(requestBodySpecMock).bodyValue(any(AddAbilityDtoRequest.class));
        verify(requestHeadersSpecMock).retrieve();
        verify(responseSpecMock).bodyToMono(Void.class);
    }

    @Test
    void getAllTechnologiesByAbilityId_shouldCallCorrectEndpoint() {
        // Arrange
        Integer abilityId = 1;
        List<Technology> expectedTechnologies = Arrays.asList(
                new Technology(1, "Java","java"),
                new Technology(2, "Spring", "spring")
        );

        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri("/technology/getAllByAbilityId?id={id}", abilityId))
                .thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToFlux(Technology.class)).thenReturn(Flux.fromIterable(expectedTechnologies));

        // Act
        Mono<List<Technology>> result = technologyClient.getAllTechnologiesByAbilityId(abilityId);

        // Assert
        StepVerifier.create(result)
                .expectNext(expectedTechnologies)
                .verifyComplete();

        verify(webClientMock).get();
        verify(requestHeadersUriSpecMock).uri("/technology/getAllByAbilityId?id={id}", abilityId);
        verify(requestHeadersSpecMock).retrieve();
        verify(responseSpecMock).bodyToFlux(Technology.class);
    }

}