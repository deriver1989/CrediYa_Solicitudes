package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.solicitud.SolicitudCredito;
import co.com.pragma.model.solicitud.gateways.SolicitudCreditoRepository;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import reactor.core.publisher.Mono;

public class SolicitudUseCase {

    private final SolicitudCreditoRepository solicitudCreditoRepository;
    private final UsuarioRepository usuarioRepository;

    public SolicitudUseCase(SolicitudCreditoRepository solicitudCreditoRepository, UsuarioRepository usuarioRepository) {
        this.solicitudCreditoRepository = solicitudCreditoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Mono<SolicitudCredito> guardarSolicitudCredito(SolicitudCredito solicitudR) {
        SolicitudCredito solicitud = new SolicitudCredito();
        solicitud.setDocumentoCliente(solicitudR.getDocumentoCliente());
        solicitud.setPlazo(solicitudR.getPlazo());
        solicitud.setMonto(solicitudR.getMonto());
        solicitud.setTipoPrestamo(solicitudR.getTipoPrestamo());

        return solicitudCreditoRepository.findByIdTipoPrestamo(solicitud.getTipoPrestamo())
                .switchIfEmpty(Mono.error(new RuntimeException("El tipo de préstamo no existe.")))
                .flatMap(tipo ->{
                    return usuarioRepository.findByCorreoElectronico(solicitudR.getUsername())
                            .switchIfEmpty(Mono.error(new RuntimeException("El solicitante no existe.")))
                            .flatMap(usuario -> {
                                if (usuario.getDocumento().equals(solicitud.getDocumentoCliente())) {
                                    return solicitudCreditoRepository.saveSolicitudCredito(solicitud);
                                } else {
                                    return Mono.error(new RuntimeException("Operación no permitida: el documento del solicitante no coincide con el usuario autenticado."));
                                }
                            });
                });
    }
}
