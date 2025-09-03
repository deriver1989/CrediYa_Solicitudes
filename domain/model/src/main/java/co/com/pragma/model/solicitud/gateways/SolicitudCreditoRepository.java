package co.com.pragma.model.solicitud.gateways;

import co.com.pragma.model.solicitud.SolicitudCredito;
import co.com.pragma.model.solicitud.TipoPrestamo;
import reactor.core.publisher.Mono;

public interface SolicitudCreditoRepository {

    Mono<SolicitudCredito> saveSolicitudCredito(SolicitudCredito solicitud);
    Mono<TipoPrestamo> findByIdTipoPrestamo(Long id);
}
