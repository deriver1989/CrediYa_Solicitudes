package co.com.pragma.usecase.solicitud.mensaje;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MensajeTest {
    @Test
    void shouldContainTipoPrestamoNoExisteMessage() {
        assertThat(Mensaje.TIPO_PRESTAMO_NO_EXISTE)
                .isNotNull()
                .isEqualTo("El tipo de préstamo no existe.");
    }

    @Test
    void shouldContainSolicitanteNoExisteMessage() {
        assertThat(Mensaje.SOLICITANTE_NO_EXISTE)
                .isNotNull()
                .isEqualTo("El solicitante no existe.");
    }

    @Test
    void shouldContainDocumentoSolicitanteNoCoincideMessage() {
        assertThat(Mensaje.DOCUMENTO_SOLICITANTE_NO_COINCIDE)
                .isNotNull()
                .isEqualTo("Operación no permitida: el documento del solicitante no coincide con el usuario autenticado.");
    }
}