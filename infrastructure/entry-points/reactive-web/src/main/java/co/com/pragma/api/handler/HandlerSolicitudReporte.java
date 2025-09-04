package co.com.pragma.api.handler;

import co.com.pragma.model.solicitud.PendienteAprobacion;
import co.com.pragma.model.solicitud.PendienteAprobacionRequest;
import co.com.pragma.usecase.solicitud.SolicitudReporteUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class HandlerSolicitudReporte {

    private final SolicitudReporteUseCase solicitudReporteUseCase;

    public Mono<ServerResponse> getReporte(ServerRequest request) {
        String name = request.queryParam("documento").orElse(null);
        String email = request.queryParam("plazo").orElse(null);
        Integer minAge = request.queryParam("tipoPrestamo").map(Integer::valueOf).orElse(null);

        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        //Pageable pageable = PageRequest.of(page, size);
        PendienteAprobacionRequest req =  new PendienteAprobacionRequest(
                request.queryParam("documento").orElse(null),
                request.queryParam("plazo").map(Integer::valueOf).orElse(null),
                null,
                request.queryParam("tipoPrestamo").map(Integer::valueOf).orElse(null)
        );

       // Flux<PendienteAprobacion> datos=solicitudReporteUseCase.guardarSolicitudCredito(req,page, size );
                //.flatMap(result -> ServerResponse.ok().bodyValue(result));
        return request.bodyToMono(PendienteAprobacionRequest.class)
                .flatMap(respuesta -> {
                    Flux<PendienteAprobacion> datos = solicitudReporteUseCase.guardarSolicitudCredito(req, page, size);

                    return ServerResponse.ok()
                            .body(datos, PendienteAprobacion.class);
                });

    }
}
