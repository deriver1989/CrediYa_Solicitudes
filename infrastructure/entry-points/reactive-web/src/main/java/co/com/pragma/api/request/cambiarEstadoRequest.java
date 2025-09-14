package co.com.pragma.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class cambiarEstadoRequest {

    @NotNull(message = "El id de la solicitud es obligatorio")
    private Long idSolicitud;

    @NotNull(message = "El estado es obligatorio")
    private Long estado;

}