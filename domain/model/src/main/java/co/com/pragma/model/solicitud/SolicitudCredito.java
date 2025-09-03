package co.com.pragma.model.solicitud;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

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
    private List<String> roles;
    private String username;

    public SolicitudCredito(String documentoCliente, Integer plazo, BigDecimal monto, Long tipoPrestamo) {
        this.documentoCliente = documentoCliente;
        this.plazo = plazo;
        this.monto = monto;
        this.tipoPrestamo = tipoPrestamo;
    }


}
