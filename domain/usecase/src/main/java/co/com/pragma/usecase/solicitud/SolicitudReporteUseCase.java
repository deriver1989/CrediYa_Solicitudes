package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.solicitud.PendienteAprobacion;
import co.com.pragma.model.solicitud.PendienteAprobacionRequest;
import co.com.pragma.model.solicitud.SolicitudCredito;
import co.com.pragma.model.solicitud.gateways.SolicitudCreditoListadoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class SolicitudReporteUseCase {

    private final SolicitudCreditoListadoRepository solicitudCreditoListadoRepository;

    public SolicitudReporteUseCase(SolicitudCreditoListadoRepository solicitudCreditoListadoRepository) {
        this.solicitudCreditoListadoRepository = solicitudCreditoListadoRepository;
    }


    public Flux<PendienteAprobacion> guardarSolicitudCredito(PendienteAprobacionRequest request, Integer page, Integer size) {

        //Mono<Long> count = solicitudCreditoListadoRepository.consulListadoCantidad(request);

        //return solicitudCreditoListadoRepository.consultarListado(request, page, size);
                /*.flatMap({ data -> {
                    return data.collectList()
                            .zipWith(count)
                            .map(tuple -> new PageImpl<>(
                                    tuple.getT1(),
                                    pageable,
                                    tuple.getT2()
                            ));
                    }

                });*/
        return solicitudCreditoListadoRepository.listarPendientes(page, size);

    }

}
