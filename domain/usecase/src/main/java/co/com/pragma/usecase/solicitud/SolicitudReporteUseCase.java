package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.solicitud.ListadoSolicitudes;
import co.com.pragma.model.solicitud.PendienteAprobacion;
import co.com.pragma.model.solicitud.PendienteAprobacionRequest;
import co.com.pragma.model.solicitud.gateways.SolicitudCreditoListadoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class SolicitudReporteUseCase {

    private final SolicitudCreditoListadoRepository solicitudCreditoListadoRepository;

    public SolicitudReporteUseCase(SolicitudCreditoListadoRepository solicitudCreditoListadoRepository) {
        this.solicitudCreditoListadoRepository = solicitudCreditoListadoRepository;
    }


    public Flux<PendienteAprobacion> generarReportePendientes(PendienteAprobacionRequest request, Integer page, Integer size) {
        return solicitudCreditoListadoRepository.listarPendientes(request,page, size);
    }

    public Mono<Long> consultarCantidad(PendienteAprobacionRequest request) {
        return solicitudCreditoListadoRepository.consulListadoCantidad(request);
    }

    public Flux<ListadoSolicitudes> listadoSolicitudes(Integer page, Integer size) {
        return solicitudCreditoListadoRepository.listadoSolicitudes(page, size);
    }

    public Mono<Long> listadoSolicitudesCantidad() {
        return solicitudCreditoListadoRepository.listadoSolicitudesCantidad();
    }

}
