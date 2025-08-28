package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.solicitud.SolicitudCredito;
import co.com.pragma.model.solicitud.TipoPrestamo;
import co.com.pragma.model.solicitud.gateways.SolicitudCreditoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class SolicitudUseCaseTest {

    @Mock
    private SolicitudCreditoRepository solicitudCreditoRepository;

    @InjectMocks
    private SolicitudUseCase solicitudUseCase;

    private SolicitudCredito solicitud;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        solicitud = new SolicitudCredito("123456", 12, new BigDecimal("5000"), 4l);
    }

    @Test
    void guardarSolicitudCredito_exitoso() {
        // Mock: existe tipo de préstamo
        when(solicitudCreditoRepository.findByIdTipoPrestamo(anyLong()))
                .thenReturn(Mono.just(new TipoPrestamo(4l, "Credito Hipotecario")));

        // Mock: guardar solicitud
        when(solicitudCreditoRepository.saveSolicitudCredito(any(SolicitudCredito.class)))
                .thenReturn(Mono.just(solicitud));

        StepVerifier.create(solicitudUseCase.guardarSolicitudCredito(solicitud))
                .expectNextMatches(result -> result.getDocumentoCliente().equals("123456")
                        && result.getMonto().equals(new BigDecimal("5000")))
                .verifyComplete();
    }

    @Test
    void guardarSolicitudCredito_tipoPrestamoNoExiste() {
        // Mock: tipo de préstamo NO existe
        when(solicitudCreditoRepository.findByIdTipoPrestamo(anyLong()))
                .thenReturn(Mono.empty());

        StepVerifier.create(solicitudUseCase.guardarSolicitudCredito(solicitud))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("El tipo de préstamo no existe."))
                .verify();
    }
}
