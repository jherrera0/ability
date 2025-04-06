package reactive_backend.ability.application.client.adapter;

import org.springframework.web.reactive.function.client.WebClient;
import reactive_backend.ability.application.http.dto.request.AddAbilityDtoRequest;
import reactive_backend.ability.application.http.dto.request.GetByNameRequest;
import reactive_backend.ability.domain.model.Technology;
import reactive_backend.ability.domain.spi.ITechnologyClientPort;
import reactive_backend.ability.domain.util.ConstRoute;
import reactor.core.publisher.Mono;

import java.util.List;

public class TechnologyClient implements ITechnologyClientPort {
    private final WebClient webClient;
    public TechnologyClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(ConstRoute.TECHNOLOGY).build();
    }


    @Override
    public Mono<List<Technology>> findTechnologiesByNames(List<String> names) {
        GetByNameRequest request = new GetByNameRequest();
        request.setNames(names);
        return webClient.post()
                .uri("/technology/getByName")
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(Technology.class)
                .collectList();
    }

    @Override
    public Mono<Void> linkTechnologiesToAbility(Integer id, List<Technology> technologies) {
        AddAbilityDtoRequest request = new AddAbilityDtoRequest();
        request.setAbilityId(id);
        request.setTechnologies(technologies);
        return webClient.post()
                .uri("/technology/addAbility")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .then();
    }
}
