package co.com.pragma.r2dbc.repository;

import co.com.pragma.r2dbc.entity.SolicitudCreditoEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SolicitudCreditoRepository extends ReactiveCrudRepository<SolicitudCreditoEntity, Long>, ReactiveQueryByExampleExecutor<SolicitudCreditoEntity> {

}
