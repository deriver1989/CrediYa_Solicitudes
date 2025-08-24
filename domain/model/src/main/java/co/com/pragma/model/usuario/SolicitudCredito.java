package co.com.pragma.model.usuario;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SolicitudCredito {

    private String documentoCliente;
    private Integer plazo;
    private BigDecimal monto;
    private Long tipoPrestamo;
}
