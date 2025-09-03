package co.com.pragma.r2dbc.adapters;

import co.com.pragma.model.solicitud.PendienteAprobacion;
import co.com.pragma.model.solicitud.PendienteAprobacionRequest;
import co.com.pragma.model.solicitud.gateways.SolicitudCreditoListadoRepository;
import co.com.pragma.r2dbc.repository.SolicitudCreditoRepository;
import co.com.pragma.r2dbc.response.PendienteAprobacionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
@Slf4j
@RequiredArgsConstructor
public class ListaSolicitudesPendientesAdapter implements SolicitudCreditoListadoRepository {

    private final SolicitudCreditoRepository solicitudCreditoRepository;

    @Override
    public Flux<PendienteAprobacion> consultarListado(PendienteAprobacionRequest criterio , Integer page, Integer size) {
        int offset = page * size;
        int limit = size;

        Mono<Long> count = consulListadoCantidad(criterio);
        Flux<PendienteAprobacionResponse> data = solicitudCreditoRepository.ListaSolicitudPendienteAprobacion(criterio.getDocumento(),
                                                                            criterio.getPlazo(),
                                                                            criterio.getTipoPrestamo(),
                                                                            limit,
                                                                            offset);

        Pageable pageable = PageRequest.of(page, size);
        return data.collectList()
            .zipWith(count)
            .map(tuple -> new PageImpl<>(
                    tuple.getT1(),
                    pageable,
                    tuple.getT2()
            ));

    }

    public Mono<Long> consulListadoCantidad(PendienteAprobacionRequest criterio) {
        return solicitudCreditoRepository.cantidadDatos(criterio.getDocumento(),
                criterio.getPlazo(),
                criterio.getTipoPrestamo()
        );
    }
}