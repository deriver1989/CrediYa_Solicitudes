package co.com.pragma.r2dbc.adapters;

import co.com.pragma.model.solicitud.SolicitudCredito;
import co.com.pragma.model.solicitud.TipoPrestamo;
import co.com.pragma.model.solicitud.gateways.EstadoSolicitudRepository;
import co.com.pragma.model.solicitud.gateways.SolicitudCreditoRepository;
import co.com.pragma.r2dbc.entity.SolicitudCreditoEntity;
import co.com.pragma.r2dbc.enums.EstadoCredito;
import co.com.pragma.r2dbc.mensaje.Mensaje;

import co.com.pragma.r2dbc.repository.SolicitudCreditoPostgreRepository;
import co.com.pragma.r2dbc.repository.TipoPrestamoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class SolicitudCreditoRepositoryAdapter implements SolicitudCreditoRepository, EstadoSolicitudRepository {

    private final SolicitudCreditoPostgreRepository solicitudCreditoPostgreRepository;
    private final TransactionalOperator txOperator;
    private final TipoPrestamoRepository tipoPrestamoRepository;

    public SolicitudCreditoRepositoryAdapter(SolicitudCreditoPostgreRepository solicitudCreditoPostgreRepository, TransactionalOperator txOperator, TipoPrestamoRepository tipoPrestamoRepository) {
        this.solicitudCreditoPostgreRepository = solicitudCreditoPostgreRepository;
        this.txOperator = txOperator;
        this.tipoPrestamoRepository = tipoPrestamoRepository;
    }


    @Override
    public Mono<SolicitudCredito> saveSolicitudCredito(SolicitudCredito solicitud) {
        SolicitudCreditoEntity entity = new SolicitudCreditoEntity(
                solicitud.getTipoPrestamo(),
                solicitud.getMonto(),
                solicitud.getPlazo(),
                solicitud.getDocumentoCliente(),
                EstadoCredito.PENDIENTE_APROBACION.name()
                );

        return solicitudCreditoPostgreRepository.save(entity)
                .as(txOperator::transactional)
                .map(saved -> new SolicitudCredito(saved.getDocumentoCliente(),
                        saved.getPlazo(),
                        saved.getMonto(),
                        saved.getTipoPrestamo()))
                .doOnError(error -> log.error(Mensaje.ERROR_GUARDAR_SOLICITUD, error))
                .doOnSuccess(user -> log.info(Mensaje.SOLICITUD_GUARDADA_EXITO));
    }

    @Override
    public Mono<TipoPrestamo> findByIdTipoPrestamo(Long id) {
        return tipoPrestamoRepository.findById(id)
                .map(entity -> TipoPrestamo.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .build()
                );
    }

    @Override
    public Mono<SolicitudCredito> findById(Long id) {
        return solicitudCreditoPostgreRepository.findById(id)
                .map(
                solicitud -> SolicitudCredito.builder()
                        .documentoCliente(solicitud.getDocumentoCliente())
                        .monto(solicitud.getMonto())
                        .plazo(solicitud.getPlazo())
                        .tipoPrestamo(solicitud.getTipoPrestamo())
                        .estado(solicitud.getEstado())
                        .build()
                );
    }

    @Override
    public Mono<SolicitudCredito> save(SolicitudCredito solicitud) {
        SolicitudCreditoEntity entity = new SolicitudCreditoEntity(
                solicitud.getId(),
                solicitud.getTipoPrestamo(),
                solicitud.getMonto(),
                solicitud.getPlazo(),
                solicitud.getDocumentoCliente(),
                solicitud.getEstado()
        );

        return solicitudCreditoPostgreRepository.save(entity)
                .as(txOperator::transactional)
                .map(saved -> new SolicitudCredito(saved.getDocumentoCliente(),
                        saved.getPlazo(),
                        saved.getMonto(),
                        saved.getTipoPrestamo()))
                .doOnError(error -> log.error(Mensaje.ERROR_GUARDAR_SOLICITUD, error))
                .doOnSuccess(user -> log.info(Mensaje.SOLICITUD_GUARDADA_EXITO));
    }
}