package co.com.pragma.r2dbc.adapters;

import co.com.pragma.model.solicitud.ListadoSolicitudes;
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


    public Flux<ListadoSolicitudes> listadoSolicitudes(Integer page, Integer size) {
        String query = """
                SELECT\s
                	monto,
                	plazo,
                	us.correo_electronico,
                	us.nombres || ' ' ||us.apellidos as nombre,
                	tp.nombre as tipo_prestamo,
                	0 as tasa_interes,
                	sc.estado,
                	us.salario_base,
                	o.deuda_total
                FROM solicitud_credito sc
                inner join usuarios us on us.documento = sc.documento_cliente\s
                inner join tipo_prestamo tp on tp.id = sc.id_tipo_prestamo
                LEFT JOIN LATERAL (
                    SELECT SUM(monto) as deuda_total
                    FROM solicitud_credito o
                    WHERE o.documento_cliente = sc.documento_cliente
                    and o.estado='APROBADO'
                    LIMIT 1
                ) o ON TRUE
                """;
        query += " LIMIT "+size+" OFFSET "+ page*size;
        System.out.println(query);
        return client.sql(query)
                .map(this::mapRowListadoSolicitudes)
                .all();
    }

    public Mono<Long> listadoSolicitudesCantidad() {
        String query = """
             SELECT\s
                	count(*) as total
                FROM solicitud_credito sc
                inner join usuarios us on us.documento = sc.documento_cliente\s
                inner join tipo_prestamo tp on tp.id = sc.id_tipo_prestamo
                LEFT JOIN LATERAL (
                    SELECT SUM(monto) as deuda_total
                    FROM solicitud_credito o
                    WHERE o.documento_cliente = sc.documento_cliente
                    and o.estado='APROBADO'
                    LIMIT 1
                ) o ON TRUE
                """;

        return client.sql(query)
                .map((row, meta) -> row.get("total", Long.class))
                .one();
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

    private ListadoSolicitudes mapRowListadoSolicitudes(Row row, RowMetadata meta) {
        return ListadoSolicitudes.builder()
                .monto(row.get("monto", Double.class))
                .plazo(row.get("plazo", Integer.class))
                .correo_electronico(row.get("correo_electronico", String.class))
                .nombre(row.get("nombre", String.class))
                .tasa_interes(row.get("tasa_interes", Double.class))
                .estado(row.get("estado", String.class))
                .salario_base(row.get("salario_base", Double.class))
                .deuda_total(row.get("deuda_total", Double.class))
                .build();
        }

}