package co.com.pragma.r2dbc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "solicitud_credito")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SolicitudCreditoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String documentoCliente;

    @Column(name = "plazo", nullable = false, precision = 5, scale = 0)
    private Integer plazo;

    @Column(name = "monto", nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_prestamo", nullable = false,
            foreignKey = @ForeignKey(name = "fk_usuario"))
    private TipoPrestamoEntity tipoPrestamo;
}