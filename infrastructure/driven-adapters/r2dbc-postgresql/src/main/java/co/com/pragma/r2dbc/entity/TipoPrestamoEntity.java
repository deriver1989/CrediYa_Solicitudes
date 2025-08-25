package co.com.pragma.r2dbc.entity;

import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.annotation.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tipo_prestamo")
public class TipoPrestamoEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("nombre")
    private String nombre;
}