package co.com.pragma.api.router;

import co.com.pragma.api.handler.HandlerSolicitud;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterSolicitud {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(HandlerSolicitud handler) {
        return route(POST("/api/v1/solicitud"), handler::guardarSolicitudCredito);
    }
}
