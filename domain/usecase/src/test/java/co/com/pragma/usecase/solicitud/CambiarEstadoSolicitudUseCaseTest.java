package co.com.pragma.usecase.solicitud;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.pragma.model.solicitud.SolicitudCredito;
import co.com.pragma.model.solicitud.gateways.ClienteSQSRepository;
import co.com.pragma.model.solicitud.gateways.EstadoSolicitudRepository;
import co.com.pragma.usecase.solicitud.mensaje.Mensaje;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

class CambiarEstadoSolicitudUseCaseTest {

    private EstadoSolicitudRepository estadoSolicitudRepository;
    private ClienteSQSRepository clienteSQSRepository;
    private CambiarEstadoSolicitudUseCase useCase;

    @BeforeEach
    void setUp() {
        estadoSolicitudRepository = Mockito.mock(EstadoSolicitudRepository.class);
        clienteSQSRepository = Mockito.mock(ClienteSQSRepository.class);
        useCase = new CambiarEstadoSolicitudUseCase(estadoSolicitudRepository, clienteSQSRepository);
    }

    @Test
    void testCambiarEstadoAprobado() {
        Long idSolicitud = 1L;
        Long estado = 1L;

        SolicitudCredito solicitud = new SolicitudCredito();
        solicitud.setId(idSolicitud);
        solicitud.setEstado("PENDIENTE");

        when(estadoSolicitudRepository.findById(idSolicitud)).thenReturn(Mono.just(solicitud));
        when(estadoSolicitudRepository.save(any(SolicitudCredito.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        SolicitudCredito result = useCase.cambiarEstadoSolicitudCredito(idSolicitud, estado).block();

        assertEquals(idSolicitud, result.getId());
        assertEquals("APROBADO", result.getEstado());

        verify(clienteSQSRepository).send(Mensaje.CAMBIO_ESTADO_SOLICITUD + "APROBADO");
    }

    @Test
    void testCambiarEstadoRechazado() {
        Long idSolicitud = 2L;
        Long estado = 0L;

        SolicitudCredito solicitud = new SolicitudCredito();
        solicitud.setId(idSolicitud);
        solicitud.setEstado("PENDIENTE");

        when(estadoSolicitudRepository.findById(idSolicitud)).thenReturn(Mono.just(solicitud));
        when(estadoSolicitudRepository.save(any(SolicitudCredito.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        SolicitudCredito result = useCase.cambiarEstadoSolicitudCredito(idSolicitud, estado).block();

        assertEquals(idSolicitud, result.getId());
        assertEquals("RECHAZADO", result.getEstado());

        verify(clienteSQSRepository).send(Mensaje.CAMBIO_ESTADO_SOLICITUD + "RECHAZADO");
    }

}