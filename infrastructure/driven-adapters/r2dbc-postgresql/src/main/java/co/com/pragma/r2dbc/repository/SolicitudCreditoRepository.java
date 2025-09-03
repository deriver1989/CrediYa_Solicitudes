package co.com.pragma.r2dbc.repository;

import co.com.pragma.r2dbc.entity.SolicitudCreditoEntity;
import co.com.pragma.r2dbc.response.PendienteAprobacionResponse;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolicitudCreditoRepository extends ReactiveCrudRepository<SolicitudCreditoEntity, Long>, ReactiveQueryByExampleExecutor<SolicitudCreditoEntity> {

    @Query("""
           SELECT documento_cliente as documento,
                	plazo,
                	monto,
                	tp.nombre as tipoPrestamo,
                	sc.estado\s
            FROM solicitud_credito sc\s
            inner join tipo_prestamo tp on tp.id = sc.id_tipo_prestamo\s
            WHERE estado = 'PENDIENTE_APROBACION'
            AND (:documento IS NULL OR documento_cliente ILIKE :documento)
            AND (:plazo IS NULL OR plazo ILIKE :plazo)
            AND (:tipoPrestamo IS NULL OR monto = :tipoPrestamo)
            LIMIT :limit OFFSET :offset
       """)
    Flux<PendienteAprobacionResponse> ListaSolicitudPendienteAprobacion(@Param("documento") String documento,
                                                                        @Param("plazo") Integer plazo,
                                                                        @Param("tipoPrestamo") Integer tipoPrestamo,
                                                                        @Param("limit") int limit,
                                                                        @Param("offset") int offset);

    @Query("""
            SELECT  count(*)
             FROM solicitud_credito sc\s
             inner join tipo_prestamo tp on tp.id = sc.id_tipo_prestamo\s
             WHERE estado = 'PENDIENTE_APROBACION'
             AND (:documento IS NULL OR documento_cliente ILIKE :documento)
             AND (:plazo IS NULL OR plazo ILIKE :plazo)
             AND (:tipoPrestamo IS NULL OR monto = :tipoPrestamo)
           """)
    Mono<Long> cantidadDatos(
            @Param("documento") String documento,
            @Param("plazo") Integer plazo,
            @Param("tipoPrestamo") Integer tipoPrestamo
    );

}
