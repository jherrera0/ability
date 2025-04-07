package reactive_backend.ability.application.http.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactive_backend.ability.application.http.dto.request.CreateAbilityDtoRequest;
import reactive_backend.ability.application.http.dto.request.GetAbilitiesByIdsDtoRequest;
import reactive_backend.ability.application.http.dto.request.ListAbilitiesRequest;
import reactive_backend.ability.application.http.mapper.IAbilityResponseMapper;
import reactive_backend.ability.application.http.mapper.ICreateAbilityDtoMapper;
import reactive_backend.ability.application.http.mapper.IPageResponseMapper;
import reactive_backend.ability.domain.api.IAbilityServicePort;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AbilityHandler implements IAbilityHandler{

    private final IAbilityServicePort abilityServicePort;
    private final ICreateAbilityDtoMapper createAbilityDtoMapper;
    private final IPageResponseMapper pageResponseMapper;
    private final IAbilityResponseMapper abilityResponseMapper;

    @Override
    public Mono<ServerResponse> createAbility(ServerRequest request) {
        return request.bodyToMono(CreateAbilityDtoRequest.class)
                .doOnNext(dto -> log.info("Datos recibidos desde Postman: {}", dto))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body cannot be empty")))
                .map(createAbilityDtoMapper::toAbility)
                .doOnNext(ability -> log.info("Datos mapeados a dominio: {}", ability))
                .flatMap(abilityServicePort::createAbility)
                .map(createAbilityDtoMapper::toDtoResponse)
                .doOnNext(response -> log.info("Datos a devolver en respuesta: {}", response))
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response)
                )
                .onErrorResume(error -> {
                    log.error("Error al procesar la solicitud: {}", error.getMessage());
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(Map.of(
                                    "error", error.getMessage(),
                                    "timestamp", Instant.now()
                            ));
                });
    }

    @Override
    public Mono<ServerResponse> getAllAbilities(ServerRequest request) {
        return request.bodyToMono(ListAbilitiesRequest.class)
                .doOnNext(dto -> log.info("Datos recibidos desde Postman para paginar: {}", dto))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body list cannot be empty")))
                .flatMap(dto -> abilityServicePort.getAllAbilities( dto.getCurrentPage(),
                          dto.getPageSize(), dto.getSortOrder(), dto.getSortField()))
                .map(pageResponseMapper::toPageResponse)
                .doOnNext(response -> log.info("Datos a devolver en respuesta de la paginacion: {}", response))
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response)
                )
                .onErrorResume(error -> {
                    log.error("Error al procesar la solicitud de paginacion: {}", error.getMessage());
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(Map.of(
                                    "error_list", error.getMessage(),
                                    "timestamp_list", Instant.now()
                            ));}

                );
    }

    @Override
    public Mono<ServerResponse> getAbilityById(ServerRequest request) {
        return request.bodyToMono(GetAbilitiesByIdsDtoRequest.class)
                .doOnNext(dto ->
                        log.info("Datos recibidos desde Postman para buscar por id: {}", dto))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body cannot be empty")))
                .flatMap(dto -> abilityServicePort.getAbilityById(dto.getIds()))
                .map(abilityResponseMapper::toDtoResponseList)
                .doOnNext(response ->
                        log.info("Datos a devolver en respuesta de la busqueda por id: {}", response))
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response)
                )
                .onErrorResume(error -> {
                    log.error("Error al procesar la solicitud de busqueda por id: {}", error.getMessage());
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(Map.of(
                                    "error", error.getMessage(),
                                    "timestamp", Instant.now()
                            ));
                });
    }
}
