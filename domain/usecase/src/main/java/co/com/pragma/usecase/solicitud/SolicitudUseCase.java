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

    public Mono<SolicitudCredito> guardarSolicitudCredito(String documento,
                                                          BigDecimal monto,
                                                          Integer plazo,
                                                          Long tipoCredito
    ) {
        SolicitudCredito solicitud = new SolicitudCredito(documento,
                plazo,
                monto,
                tipoCredito);
        return solicitudCreditoRepository.saveSolicitudCredito(solicitud);
    }
}
