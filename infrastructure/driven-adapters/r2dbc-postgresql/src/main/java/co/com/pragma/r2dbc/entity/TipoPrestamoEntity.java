package co.com.pragma.r2dbc.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_prestamo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TipoPrestamoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;
}