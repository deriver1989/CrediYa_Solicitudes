package co.com.pragma.model.solicitud;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudCreditoTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        SolicitudCredito solicitud = new SolicitudCredito();
        solicitud.setDocumentoCliente("123456");
        solicitud.setPlazo(12);
        solicitud.setMonto(new BigDecimal("5000"));
        solicitud.setTipoPrestamo(1L);

        assertEquals("123456", solicitud.getDocumentoCliente());
        assertEquals(12, solicitud.getPlazo());
        assertEquals(new BigDecimal("5000"), solicitud.getMonto());
        assertEquals(1L, solicitud.getTipoPrestamo());
    }

    @Test
    void testAllArgsConstructor() {
        SolicitudCredito solicitud = new SolicitudCredito("654321", 24, new BigDecimal("10000"), 2L);

        assertEquals("654321", solicitud.getDocumentoCliente());
        assertEquals(24, solicitud.getPlazo());
        assertEquals(new BigDecimal("10000"), solicitud.getMonto());
        assertEquals(2L, solicitud.getTipoPrestamo());
    }

    @Test
    void testBuilder() {
        SolicitudCredito solicitud = SolicitudCredito.builder()
                .documentoCliente("987654")
                .plazo(36)
                .monto(new BigDecimal("15000"))
                .tipoPrestamo(3L)
                .build();

        assertEquals("987654", solicitud.getDocumentoCliente());
        assertEquals(36, solicitud.getPlazo());
        assertEquals(new BigDecimal("15000"), solicitud.getMonto());
        assertEquals(3L, solicitud.getTipoPrestamo());
    }

    @Test
    void testBuilderToBuilder() {
        SolicitudCredito solicitud = SolicitudCredito.builder()
                .documentoCliente("111111")
                .plazo(48)
                .monto(new BigDecimal("20000"))
                .tipoPrestamo(4L)
                .build();

        SolicitudCredito nueva = solicitud.toBuilder().plazo(60).build();

        assertEquals("111111", nueva.getDocumentoCliente());
        assertEquals(60, nueva.getPlazo()); // cambiado
        assertEquals(new BigDecimal("20000"), nueva.getMonto()); // mismo valor
        assertEquals(4L, nueva.getTipoPrestamo());
    }
}
