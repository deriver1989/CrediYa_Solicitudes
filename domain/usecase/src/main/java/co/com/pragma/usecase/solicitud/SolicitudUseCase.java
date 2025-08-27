package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.solicitud.SolicitudCredito;
import co.com.pragma.model.solicitud.gateways.SolicitudCreditoRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

//@RequiredArgsConstructor
public class SolicitudUseCase {

    private final SolicitudCreditoRepository solicitudCreditoRepository;

    public SolicitudUseCase(SolicitudCreditoRepository solicitudCreditoRepository) {
        this.solicitudCreditoRepository = solicitudCreditoRepository;
    }

    public Mono<SolicitudCredito> guardarSolicitudCredito(SolicitudCredito solicitudR) {
        SolicitudCredito solicitud = new SolicitudCredito(solicitudR.getDocumentoCliente(),
                solicitudR.getPlazo(),
                solicitudR.getMonto(),
                solicitudR.getTipoPrestamo());
        return solicitudCreditoRepository.findByIdTipoPrestamo(solicitud.getTipoPrestamo())
                .switchIfEmpty(Mono.error(new RuntimeException("El tipo de préstamo no existe.")))
                .flatMap(tipo ->{
                    return solicitudCreditoRepository.saveSolicitudCredito(solicitud);
                });
    }
}
