package reactive_backend.ability.application.http.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface IBootcampHandler {
    Mono<ServerResponse> addBootcamp(ServerRequest request);
    Mono<ServerResponse> getAllBootcamps(ServerRequest request);
}
