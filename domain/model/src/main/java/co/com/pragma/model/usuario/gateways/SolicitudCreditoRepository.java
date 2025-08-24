package co.com.pragma.model.usuario.gateways;

import co.com.pragma.model.usuario.SolicitudCredito;
import reactor.core.publisher.Mono;

public interface SolicitudCreditoRepository {

    Mono<SolicitudCredito> saveSolicitudCredito(SolicitudCredito solicitud);
}
