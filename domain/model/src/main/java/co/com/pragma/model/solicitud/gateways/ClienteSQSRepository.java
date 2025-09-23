package co.com.pragma.model.solicitud.gateways;

import reactor.core.publisher.Mono;

public interface ClienteSQSRepository {

    void send(String message);
}
