package co.com.pragma.r2dbc.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PendienteAprobacionResponse {

    private String documento;
    private Integer plazo;
    private BigDecimal monto;
    private String tipoPrestamo;
    private String estado;
}