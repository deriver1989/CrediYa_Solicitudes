package co.com.pragma.api.handler;

import co.com.pragma.api.mensaje.Mensaje;
import co.com.pragma.model.solicitud.ListadoSolicitudes;
import co.com.pragma.model.solicitud.PendienteAprobacion;
import co.com.pragma.model.solicitud.PendienteAprobacionRequest;
import co.com.pragma.usecase.solicitud.SolicitudReporteUseCase;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HandlerSolicitudReporte {

    private final SolicitudReporteUseCase solicitudReporteUseCase;

    public Mono<ServerResponse> getReporte(ServerRequest request) {

        return request.bodyToMono(PendienteAprobacionRequest.class)
                .flatMap(respuesta -> {
                    int page = Integer.parseInt(request.queryParam("page").orElse("0"));
                    int size = Integer.parseInt(request.queryParam("size").orElse("10"));

                    PendienteAprobacionRequest req =  new PendienteAprobacionRequest(
                            request.queryParam("documento").orElse(null),
                            request.queryParam("plazo").map(Integer::valueOf).orElse(null),
                            null,
                            request.queryParam("tipoPrestamo").map(Integer::valueOf).orElse(null)
                    );

                    Mono<Long> cantidad = solicitudReporteUseCase.consultarCantidad(req); //Consulta de cantidad de datos
                    Flux<PendienteAprobacion> datos = solicitudReporteUseCase.generarReportePendientes(req, page, size); // Consulta de informacion por pagina
                    Pageable pageable = PageRequest.of(page, size);
                    return datos.collectList()
                            .zipWith(cantidad)
                            .flatMap(tuple -> {
                                PageImpl<PendienteAprobacion> pageResponse = new PageImpl<>( //creando pageable con informacion
                                        tuple.getT1(),
                                        pageable,
                                        tuple.getT2()
                                );
                                return ServerResponse.ok()
                                        .bodyValue(pageResponse);
                            });
                })
                .onErrorResume(e -> {
                    return ServerResponse.badRequest().bodyValue(generarJsonMsg("Error general",Mensaje.ERROR_GENERAR_REPORTE));
                });
    }


    public Mono<ServerResponse> getReporteListadoSolicitudes(ServerRequest request) {

        return request.bodyToMono(PendienteAprobacionRequest.class)
                .flatMap(respuesta -> {
                    int page = Integer.parseInt(request.queryParam("page").orElse("0"));
                    int size = Integer.parseInt(request.queryParam("size").orElse("10"));

                    Mono<Long> cantidad = solicitudReporteUseCase.listadoSolicitudesCantidad(); //Consulta de cantidad de datos
                    Flux<ListadoSolicitudes> datos = solicitudReporteUseCase.listadoSolicitudes(page, size); // Consulta de informacion por pagina
                    Pageable pageable = PageRequest.of(page, size);
                    return datos.collectList()
                            .zipWith(cantidad)
                            .flatMap(tuple -> {
                                PageImpl<ListadoSolicitudes> pageResponse = new PageImpl<>( //creando pageable con informacion
                                        tuple.getT1(),
                                        pageable,
                                        tuple.getT2()
                                );
                                return ServerResponse.ok()
                                        .bodyValue(pageResponse);
                            });
                })
                .onErrorResume(e -> {
                    return ServerResponse.badRequest().bodyValue(generarJsonMsg("Error general",Mensaje.ERROR_GENERAR_REPORTE));
                });
    }


    private Map<String, Object> generarJsonMsg(String error, String detalle){
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", error);
        errorResponse.put("detalle",detalle);
        return errorResponse;
    }
}
