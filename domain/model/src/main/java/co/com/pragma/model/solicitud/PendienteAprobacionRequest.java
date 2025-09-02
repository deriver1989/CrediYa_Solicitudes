package co.com.pragma.model.solicitud;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PendienteAprobacionRequest {

    private String documento;
    private Integer plazo;
    private BigDecimal monto;
    private Integer tipoPrestamo;
}