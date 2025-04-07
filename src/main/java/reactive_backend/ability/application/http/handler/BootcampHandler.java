package reactive_backend.ability.application.http.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactive_backend.ability.application.http.dto.request.AddBootcampDtoRequest;
import reactive_backend.ability.domain.api.IBootcampServicePort;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootcampHandler implements IBootcampHandler {
    private final IBootcampServicePort bootcampServicePort;
    @Override
    public Mono<ServerResponse> addBootcamp(ServerRequest request) {
         return request.bodyToMono(AddBootcampDtoRequest.class)
                .doOnNext(dto -> log.info("Datos recibidos desde Postman para crear bootcamp: {}", dto))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body cannot be empty")))
                .flatMapMany(dto -> bootcampServicePort.addBootcamp(dto.getBootcampId(),
                        dto.getAbilitiesIds()))
                .collectList()
                .doOnNext(dto -> log.info("Datos a devolver en respuesta de bootcamp creado: {}", dto))
                .flatMap(bootcamp -> ServerResponse.ok()
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .bodyValue(bootcamp)
                ).onErrorResume(error -> {
                    log.error("Error al procesar la solicitud de crear bootcamp: {}", error.getMessage());
                    return ServerResponse.badRequest()
                            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                            .bodyValue(Map.of(
                                    "error", error.getMessage(),
                                    "timestamp", java.time.Instant.now()
                            ));
                });
    }

}
