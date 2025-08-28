package co.com.pragma.r2dbc.adapters;

import co.com.pragma.model.solicitud.SolicitudCredito;
import co.com.pragma.model.solicitud.TipoPrestamo;
import co.com.pragma.r2dbc.entity.SolicitudCreditoEntity;
import co.com.pragma.r2dbc.entity.TipoPrestamoEntity;
import co.com.pragma.r2dbc.enums.EstadoCredito;
import co.com.pragma.r2dbc.repository.SolicitudCreditoRepository;
import co.com.pragma.r2dbc.repository.TipoPrestamoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class SolicitudCreditoRepositoryAdapter implements co.com.pragma.model.solicitud.gateways.SolicitudCreditoRepository {

    private final SolicitudCreditoRepository solicitudCreditoRepository;
    private final TransactionalOperator txOperator;
    private final TipoPrestamoRepository tipoPrestamoRepository;

    public SolicitudCreditoRepositoryAdapter(SolicitudCreditoRepository solicitudCreditoRepository,
                                             TransactionalOperator txOperator,
                                             TipoPrestamoRepository tipoPrestamoRepository) {
        this.solicitudCreditoRepository = solicitudCreditoRepository;
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

        return solicitudCreditoRepository.save(entity)
                .as(txOperator::transactional)
                .map(saved -> new SolicitudCredito(saved.getDocumentoCliente(),
                        saved.getPlazo(),
                        saved.getMonto(),
                        saved.getTipoPrestamo()))
                .doOnError(error -> log.error("Error al guardar la solicitud", error))
                .doOnSuccess(user -> log.info("Proceso finalizado con éxito, la solicitud ha sido guardada."));
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

}