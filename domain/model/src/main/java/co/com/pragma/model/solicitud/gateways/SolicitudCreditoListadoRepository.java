package co.com.pragma.model.solicitud.gateways;

import co.com.pragma.model.solicitud.PendienteAprobacion;
import co.com.pragma.model.solicitud.PendienteAprobacionRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface SolicitudCreditoListadoRepository {

    Flux<PendienteAprobacion> consultarListado(PendienteAprobacionRequest criterio, Integer page, Integer size);
}
