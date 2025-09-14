package co.com.pragma.model.solicitud.gateways;

import co.com.pragma.model.solicitud.SolicitudCredito;
import reactor.core.publisher.Mono;

public interface EstadoSolicitudRepository {

    Mono<SolicitudCredito> findById(Long id);
    Mono<SolicitudCredito> save(SolicitudCredito solicitud);
}
