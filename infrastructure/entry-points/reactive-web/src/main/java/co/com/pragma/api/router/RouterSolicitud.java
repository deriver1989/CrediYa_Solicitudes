package co.com.pragma.api.router;

import co.com.pragma.api.handler.HandlerSolicitud;
import co.com.pragma.api.request.SolicitudCreditoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterSolicitud {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/solicitud",
                    method = RequestMethod.POST,
                    beanClass = HandlerSolicitud.class,
                    beanMethod = "guardarSolicitudCredito",
                    operation = @Operation(
                            operationId = "guardarSolicitudCredito",
                            summary = "Guardar una nueva solicitud de crédito.",
                            description = "Crea una nueva solicitu de credito en el sistema",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Datos del usuario y solicitud a guardar",
                                    content = @Content(
                                            schema = @Schema(implementation = SolicitudCreditoRequest.class),
                                            examples = @ExampleObject(
                                                    value = """
                                                            {
                                                              "documentoCliente": "1032456789",
                                                              "plazo": 24,
                                                              "monto": 15000000.50,
                                                              "tipoPrestamo": 4
                                                            }
                                                            """
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Solicitud creado exitosamente",
                                            content = @Content(schema = @Schema(implementation = SolicitudCreditoRequest.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error de validación en la solicitud"
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor"
                                    )
                            }
                    )
            )

    })
    public RouterFunction<ServerResponse> routerFunction(HandlerSolicitud handler) {
        return route(POST("/api/v1/solicitud"), handler::guardarSolicitudCredito);
    }
}
