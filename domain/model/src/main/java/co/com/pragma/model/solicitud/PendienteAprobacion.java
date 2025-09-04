package co.com.pragma.model.solicitud;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendienteAprobacion {

    private String documento;
    private Integer plazo;
    private BigDecimal monto;
    private String tipoPrestamo;
    private String estado;
}