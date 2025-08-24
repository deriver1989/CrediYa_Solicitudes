package co.com.pragma.usecase.usuario;

import co.com.pragma.model.usuario.SolicitudCredito;
import co.com.pragma.model.usuario.gateways.SolicitudCreditoRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

//@RequiredArgsConstructor
public class UsuarioUseCase {

    private final SolicitudCreditoRepository solicitudCreditoRepository;

    public UsuarioUseCase(SolicitudCreditoRepository solicitudCreditoRepository) {
        this.solicitudCreditoRepository = solicitudCreditoRepository;
    }

    public Mono<SolicitudCredito> guardarSolicitudCredito(String documento,
                                                          BigDecimal monto,
                                                          Integer plazo,
                                                          Long tipoCredito
    ) {
        SolicitudCredito solicitud = new SolicitudCredito(documento,
                monto,
                plazo,
                tipoCredito);
        return solicitudCreditoRepository.saveSolicitudCredito(solicitud);
    }
}
