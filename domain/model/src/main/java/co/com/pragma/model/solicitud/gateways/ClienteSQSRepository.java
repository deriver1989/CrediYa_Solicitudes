package co.com.pragma.model.solicitud.gateways;

import reactor.core.publisher.Mono;

public interface ClienteSQSRepository {

    Mono<String> send(String message);
}
