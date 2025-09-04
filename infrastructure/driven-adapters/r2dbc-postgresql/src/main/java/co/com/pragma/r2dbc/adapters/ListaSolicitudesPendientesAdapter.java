package co.com.pragma.r2dbc.adapters;

import co.com.pragma.model.solicitud.PendienteAprobacion;
import co.com.pragma.model.solicitud.PendienteAprobacionRequest;
import co.com.pragma.model.solicitud.gateways.SolicitudCreditoListadoRepository;
import co.com.pragma.r2dbc.repository.SolicitudCreditoRepository;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
@Slf4j
@RequiredArgsConstructor
public class ListaSolicitudesPendientesAdapter implements SolicitudCreditoListadoRepository {

    private final SolicitudCreditoRepository solicitudCreditoRepository;
    private final DatabaseClient client;

    public Mono<Long> consulListadoCantidad(PendienteAprobacionRequest criterio) {

        String query = """
                SELECT count(*) as total
                FROM solicitud_credito sc
                INNER JOIN tipo_prestamo tp ON tp.id = sc.id_tipo_prestamo
                WHERE sc.estado = 'PENDIENTE_APROBACION' 
                """
                + (criterio != null && criterio.getDocumento() != null ? " and sc.documento_cliente ILIKE '%"+criterio.getDocumento()+"%'" : "")
                + (criterio != null && criterio.getTipoPrestamo() != null ? " and tp.id = "+criterio.getTipoPrestamo() : "")
                + (criterio != null && criterio.getPlazo() != null ? " and sc.plazo = "+criterio.getPlazo() : "");
        return client.sql(query)
                .map((row, meta) -> row.get("total", Long.class))
                .one();
    }


    public Flux<PendienteAprobacion> listarPendientes(PendienteAprobacionRequest criterio, Integer page, Integer size) {
        String query = """
                SELECT sc.documento_cliente as documento,
                       sc.plazo,
                       sc.monto,
                       tp.nombre as tipoPrestamo,
                       sc.estado
                FROM solicitud_credito sc
                INNER JOIN tipo_prestamo tp ON tp.id = sc.id_tipo_prestamo
                WHERE sc.estado = 'PENDIENTE_APROBACION' 
                """
                + (criterio != null && criterio.getDocumento() != null ? " and sc.documento_cliente ILIKE '%"+criterio.getDocumento()+"%'" : "")
                + (criterio != null && criterio.getTipoPrestamo() != null ? " and tp.id = "+criterio.getTipoPrestamo() : "")
                + (criterio != null && criterio.getPlazo() != null ? " and sc.plazo = "+criterio.getPlazo() : "");
        query += " LIMIT "+size+" OFFSET "+ page*size;
        System.out.println(query);
        return client.sql(query)
                .map(this::mapRow)
                .all();
    }

    private PendienteAprobacion mapRow(Row row, RowMetadata meta) {
        return PendienteAprobacion.builder()
                .documento(row.get("documento", String.class))
                .plazo(row.get("plazo", Integer.class))
                .monto(row.get("monto", java.math.BigDecimal.class))
                .tipoPrestamo(row.get("tipoPrestamo", String.class))
                .estado(row.get("estado", String.class))
                .build();
    }
}