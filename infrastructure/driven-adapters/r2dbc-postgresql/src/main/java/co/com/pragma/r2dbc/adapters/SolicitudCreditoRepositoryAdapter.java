package co.com.pragma.r2dbc.adapters;

import co.com.pragma.model.usuario.SolicitudCredito;
import co.com.pragma.r2dbc.entity.SolicitudCreditoEntity;
import co.com.pragma.r2dbc.entity.TipoPrestamoEntity;
import co.com.pragma.r2dbc.repository.SolicitudCreditoRepository;
import co.com.pragma.r2dbc.repository.TipoPrestamoRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Component
public class SolicitudCreditoRepositoryAdapter implements co.com.pragma.model.usuario.gateways.SolicitudCreditoRepository {

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
        SolicitudCreditoEntity entity = SolicitudCreditoEntity.builder()
                .documentoCliente(solicitud.getDocumentoCliente())
                .plazo(solicitud.getPlazo())
                .monto(solicitud.getMonto())
                .tipoPrestamo(TipoPrestamoEntity.builder().id(solicitud.getTipoPrestamo()).build())
                .build();
        return tipoPrestamoRepository.existsById(entity.getTipoPrestamo().getId())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RuntimeException("El tipo de préstamo no existe."));
                    }
                    return solicitudCreditoRepository.save(entity)
                            .as(txOperator::transactional)
                            .map(saved -> new SolicitudCredito(saved.getDocumentoCliente(),
                                    saved.getPlazo(),
                                    saved.getMonto(),
                                    saved.getTipoPrestamo().getId()));
                });


    }

}