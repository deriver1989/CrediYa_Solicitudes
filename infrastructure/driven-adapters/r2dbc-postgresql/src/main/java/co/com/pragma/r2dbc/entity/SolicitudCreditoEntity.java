package co.com.pragma.r2dbc.entity;

import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.annotation.Id;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "solicitud_credito")
public class SolicitudCreditoEntity {

    @Id
    private Long id;

    private String documentoCliente;

    @Column("plazo")
    private Integer plazo;

    @Column("monto")
    private BigDecimal monto;

    @Column("id_tipo_prestamo")
    private Long tipoPrestamo;

    @Column("estado")
    private String estado;

    public SolicitudCreditoEntity(Long tipoPrestamo, BigDecimal monto, Integer plazo, String documentoCliente, String estado) {
        this.tipoPrestamo = tipoPrestamo;
        this.monto = monto;
        this.plazo = plazo;
        this.documentoCliente = documentoCliente;
        this.estado =  estado;
    }
}