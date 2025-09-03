package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.solicitud.SolicitudCredito;
import co.com.pragma.model.solicitud.TipoPrestamo;
import co.com.pragma.model.solicitud.gateways.SolicitudCreditoRepository;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.usecase.solicitud.mensaje.Mensaje;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class SolicitudUseCaseTest {

    @Mock
    private SolicitudCreditoRepository solicitudCreditoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

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

        solicitud.setUsername("username@username.com");

        Usuario usuario = new Usuario();
        usuario.setNombres("Juan");
        usuario.setApellidos("Diaz");
        usuario.setDocumento("123456");
        usuario.setCorreo_electronico("prueba@prueba.com");

        // Mock: existe tipo de préstamo
        when(solicitudCreditoRepository.findByIdTipoPrestamo(anyLong()))
                .thenReturn(Mono.just(new TipoPrestamo(4l, "Credito Hipotecario")));

        when(usuarioRepository.findByCorreoElectronico(anyString()))
                .thenReturn(Mono.just(usuario));
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
        when(solicitudCreditoRepository.findByIdTipoPrestamo(anyLong()))
                .thenReturn(Mono.empty());

        StepVerifier.create(solicitudUseCase.guardarSolicitudCredito(solicitud))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals(Mensaje.TIPO_PRESTAMO_NO_EXISTE))
                .verify();
    }

    @Test
    void guardarSolicitudCredito_solicitante_nocoindice() {
        solicitud.setUsername("username@username.com");

        Usuario usuario = new Usuario();
        usuario.setNombres("Juan");
        usuario.setApellidos("Diaz");
        usuario.setDocumento("123456777");
        usuario.setCorreo_electronico("prueba@prueba.com");

        when(solicitudCreditoRepository.findByIdTipoPrestamo(anyLong()))
                .thenReturn(Mono.just(new TipoPrestamo(4l, "Credito Hipotecario")));

        when(usuarioRepository.findByCorreoElectronico(anyString()))
                .thenReturn(Mono.just(usuario));

        when(solicitudCreditoRepository.saveSolicitudCredito(any(SolicitudCredito.class)))
                .thenReturn(Mono.just(solicitud));

        StepVerifier.create(solicitudUseCase.guardarSolicitudCredito(solicitud))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals(Mensaje.DOCUMENTO_SOLICITANTE_NO_COINCIDE))
                .verify();
    }

    @Test
    void guardarSolicitudCredito_solicitante_no_existee() {
        solicitud.setUsername("username@username.com");

        Usuario usuario = new Usuario();
        usuario.setNombres("Juan");
        usuario.setApellidos("Diaz");
        usuario.setDocumento("123456777");
        usuario.setCorreo_electronico("prueba@prueba.com");

        when(solicitudCreditoRepository.findByIdTipoPrestamo(anyLong()))
                .thenReturn(Mono.just(new TipoPrestamo(4l, "Credito Hipotecario")));

        when(usuarioRepository.findByCorreoElectronico(anyString()))
                .thenReturn(Mono.empty());

        when(solicitudCreditoRepository.saveSolicitudCredito(any(SolicitudCredito.class)))
                .thenReturn(Mono.just(solicitud));

        StepVerifier.create(solicitudUseCase.guardarSolicitudCredito(solicitud))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals(Mensaje.SOLICITANTE_NO_EXISTE))
                .verify();
    }
}
