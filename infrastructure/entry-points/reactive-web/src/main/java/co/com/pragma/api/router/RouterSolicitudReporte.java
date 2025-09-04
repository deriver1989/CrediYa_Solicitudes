package co.com.pragma.api.router;

import co.com.pragma.api.handler.HandlerSolicitudReporte;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterSolicitudReporte {
    @Bean

    public RouterFunction<ServerResponse> routerFunctionReporte(HandlerSolicitudReporte handler) {
        return route(GET("/api/v1/reporte"), handler::getReporte)
        .and(route(GET("/api/v1/reporte/listadosolicitudes"), handler::getReporteListadoSolicitudes));
    }
}
