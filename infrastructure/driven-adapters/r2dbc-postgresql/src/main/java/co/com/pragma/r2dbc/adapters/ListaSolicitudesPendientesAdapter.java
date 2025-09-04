package co.com.pragma.r2dbc.adapters;

import co.com.pragma.model.solicitud.PendienteAprobacion;
import co.com.pragma.model.solicitud.PendienteAprobacionRequest;
import co.com.pragma.model.solicitud.gateways.SolicitudCreditoListadoRepository;
import co.com.pragma.r2dbc.repository.SolicitudCreditoRepository;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
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

    @Override
    public /*Mono<Page<PendienteAprobacion>>*/Flux<PendienteAprobacion> consultarListado(PendienteAprobacionRequest criterio , Integer page, Integer size) {

        int offset = page * size;
        int limit = size;

        Mono<Long> count = consulListadoCantidad(criterio);
        //Flux<PendienteAprobacion> data =
                return solicitudCreditoRepository.ListaSolicitudPendienteAprobacion(
                        criterio.getDocumento(),
                        criterio.getPlazo(),
                        criterio.getTipoPrestamo(),
                        limit,
                        offset
                ).map(d-> new PendienteAprobacion(d.getDocumento(),d.getPlazo(),d.getMonto(),d.getTipoPrestamo(),d.getEstado()));

       // Pageable pageable = PageRequest.of(page, size);

       /* return data.collectList()
                .zipWith(count)
                .map(tuple -> new PageImpl<>(
                        tuple.getT1(), // lista de responses
                        pageable,
                        tuple.getT2()
                ));*/
    }

    public Mono<Long> consulListadoCantidad(PendienteAprobacionRequest criterio) {
        return solicitudCreditoRepository.cantidadDatos(criterio.getDocumento(),
                criterio.getPlazo(),
                criterio.getTipoPrestamo()
        );
    }


    public Flux<PendienteAprobacion> listarPendientes(Integer page, Integer size) {
        String query = """
                SELECT sc.documento_cliente as documento,
                       sc.plazo,
                       sc.monto,
                       tp.nombre as tipoPrestamo,
                       sc.estado
                FROM solicitud_credito sc
                INNER JOIN tipo_prestamo tp ON tp.id = sc.id_tipo_prestamo
                WHERE sc.estado = 'PENDIENTE_APROBACION' 
                """;
        query += " LIMIT "+size+" OFFSET "+ page*size;

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