package co.com.pragma.api.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SolicitudCreditoRequest {

    @NotNull(message = "El documento es obligatorio")
    @NotBlank(message = "El documento no puede estar vacío")
    private String documentoCliente;

    @NotNull(message = "El plazo es obligatorio")
    @DecimalMin(value = "1", inclusive = true, message = "El plazo debe ser mayor o igual a 1")
    private Integer plazo;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "1", inclusive = true, message = "El monto debe ser mayor o igual a 1")
    private BigDecimal monto;

    @NotNull(message = "El tipo de prestamo es obligatorio")
    private Long tipoPrestamo;
}