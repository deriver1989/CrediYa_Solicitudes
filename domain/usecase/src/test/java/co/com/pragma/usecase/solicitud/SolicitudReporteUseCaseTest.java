package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.solicitud.*;
import co.com.pragma.model.solicitud.gateways.SolicitudCreditoListadoRepository;
import co.com.pragma.model.solicitud.gateways.SolicitudCreditoRepository;
import co.com.pragma.model.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class SolicitudReporteUseCaseTest {

    @Mock
    private SolicitudCreditoListadoRepository solicitudCreditoListadoRepository;

    @InjectMocks
    private SolicitudReporteUseCase solicitudReporteUseCase;

    Integer page;
    Integer size;
    PendienteAprobacionRequest request;
    PendienteAprobacion respuesta;
    ListadoSolicitudes listado;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        page = 0;
        size = 10;
        request = new PendienteAprobacionRequest("123456", 12, new BigDecimal(500000), 1 );
        respuesta = new PendienteAprobacion("123456",12, new BigDecimal(500000), "Préstamo Hipotecario", "APROBADO" );
        listado =  new ListadoSolicitudes(50000.0, 12, "prueba@hotmail.com", "Juan Diaz", 12.0, "Préstamo Hipotecario", 23000.0, 6000000.0 );
    }

    @Test
    void guardarSolicitudCredito() {

        when(solicitudCreditoListadoRepository.listarPendientes(any(PendienteAprobacionRequest.class), anyInt(), anyInt()))
                .thenReturn(Flux.just(respuesta));

        StepVerifier.create(solicitudReporteUseCase.generarReportePendientes(request, page, size))
                .expectNextMatches(result -> result.getDocumento().equals("123456")
                        && result.getMonto().equals(new BigDecimal("500000")))
                .verifyComplete();
    }

    @Test
    void consultarCantidad() {

        when(solicitudCreditoListadoRepository.consulListadoCantidad(any(PendienteAprobacionRequest.class)))
                .thenReturn(Mono.just(5L));

        StepVerifier.create(solicitudReporteUseCase.consultarCantidad(request))
                .expectNext(5L)
                .verifyComplete();
    }


    @Test
    void listadoSolicitudes() {
        when(solicitudCreditoListadoRepository.listadoSolicitudes(anyInt(), anyInt()))
                .thenReturn(Flux.just(listado));

        StepVerifier.create(solicitudReporteUseCase.listadoSolicitudes(page, size))
                .expectNextMatches(result -> result.getCorreo_electronico().equals("prueba@hotmail.com"))
                .verifyComplete();
    }

    @Test
    void listadoSolicitudesCantidad() {
        when(solicitudCreditoListadoRepository.listadoSolicitudesCantidad())
                .thenReturn(Mono.just(5L));

        StepVerifier.create(solicitudReporteUseCase.listadoSolicitudesCantidad())
                .expectNext(5L)
                .verifyComplete();
    }
}