package co.com.pragma.model.solicitud;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListadoSolicitudes {

    private Double monto;
    private Integer plazo;
    private String correo_electronico;
    private String nombre;
    private Double tasa_interes;
    private String estado;
    private Double salario_base;
    private Double deuda_total;
}