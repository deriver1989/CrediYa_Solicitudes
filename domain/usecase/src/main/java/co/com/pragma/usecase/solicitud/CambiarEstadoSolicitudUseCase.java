package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.solicitud.SolicitudCredito;
import co.com.pragma.model.solicitud.gateways.ClienteSQSRepository;
import co.com.pragma.model.solicitud.gateways.EstadoSolicitudRepository;
import co.com.pragma.usecase.solicitud.mensaje.Mensaje;
import reactor.core.publisher.Mono;

public class CambiarEstadoSolicitudUseCase {

    private final EstadoSolicitudRepository estadoSolicitudRepository;
    private final ClienteSQSRepository clienteSQSRepository;

    public CambiarEstadoSolicitudUseCase(EstadoSolicitudRepository estadoSolicitudRepository, ClienteSQSRepository clienteSQSRepository) {
        this.estadoSolicitudRepository = estadoSolicitudRepository;
        this.clienteSQSRepository = clienteSQSRepository;
    }


    public Mono<SolicitudCredito> cambiarEstadoSolicitudCredito(Long idSolicitud, Long estado) {
        return estadoSolicitudRepository.findById(idSolicitud)
                .flatMap(solicitud -> {

                    String nuevoEstado =  (estado==1? "APROBADO": "RECHAZADO");
                    solicitud.setEstado(nuevoEstado);
                    solicitud.setId(idSolicitud);
                    return estadoSolicitudRepository.save(solicitud)
                            .doOnSuccess(saved -> {
                                if ("Aprobado".equalsIgnoreCase(nuevoEstado) || "Rechazado".equalsIgnoreCase(nuevoEstado)) {
                                    //clienteSQSRepository.send(Mensaje.CAMBIO_ESTADO_SOLICITUD+nuevoEstado);
                                }
                            });
                });
    }

}
