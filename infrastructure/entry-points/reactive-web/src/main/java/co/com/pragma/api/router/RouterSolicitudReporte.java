package co.com.pragma.api.router;

import co.com.pragma.api.handler.HandlerSolicitudReporte;
import co.com.pragma.model.solicitud.PendienteAprobacionRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterSolicitudReporte {
    @Bean

    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/reporte",
                    method = GET,
                    beanClass = HandlerSolicitudReporte.class,
                    beanMethod = "guardarUsuarioNuevo",
                    operation = @Operation(
                            operationId = "getReporte",
                            summary = "Listado de Solicitudes",
                            description = "Muestra un listado paginado de solicitudes hechas",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Solicitudes realizadas",
                                    content = @Content(
                                            schema = @Schema(implementation = PendienteAprobacionRequest.class),
                                            examples = @ExampleObject(
                                                    value = "{\n" +
                                                            "    \"content\": [\n" +
                                                            "        {\n" +
                                                            "            \"documento\": \"1032456789\",\n" +
                                                            "            \"plazo\": 24,\n" +
                                                            "            \"monto\": 15000000.50,\n" +
                                                            "            \"tipoPrestamo\": \"Préstamo Educativo\",\n" +
                                                            "            \"estado\": \"PENDIENTE_APROBACION\"\n" +
                                                            "        },\n" +
                                                            "        {\n" +
                                                            "            \"documento\": \"1032456789\",\n" +
                                                            "            \"plazo\": 24,\n" +
                                                            "            \"monto\": 15000000.50,\n" +
                                                            "            \"tipoPrestamo\": \"Préstamo Educativo\",\n" +
                                                            "            \"estado\": \"PENDIENTE_APROBACION\"\n" +
                                                            "        },\n" +
                                                            "        {\n" +
                                                            "            \"documento\": \"1032456789\",\n" +
                                                            "            \"plazo\": 24,\n" +
                                                            "            \"monto\": 15000000.50,\n" +
                                                            "            \"tipoPrestamo\": \"Préstamo Educativo\",\n" +
                                                            "            \"estado\": \"PENDIENTE_APROBACION\"\n" +
                                                            "        },\n" +
                                                            "        {\n" +
                                                            "            \"documento\": \"1032456789\",\n" +
                                                            "            \"plazo\": 24,\n" +
                                                            "            \"monto\": 15000000.50,\n" +
                                                            "            \"tipoPrestamo\": \"Préstamo Educativo\",\n" +
                                                            "            \"estado\": \"PENDIENTE_APROBACION\"\n" +
                                                            "        }\n" +
                                                            "    ],\n" +
                                                            "    \"pageable\": {\n" +
                                                            "        \"pageNumber\": 0,\n" +
                                                            "        \"pageSize\": 20,\n" +
                                                            "        \"sort\": {\n" +
                                                            "            \"empty\": true,\n" +
                                                            "            \"sorted\": false,\n" +
                                                            "            \"unsorted\": true\n" +
                                                            "        },\n" +
                                                            "        \"offset\": 0,\n" +
                                                            "        \"paged\": true,\n" +
                                                            "        \"unpaged\": false\n" +
                                                            "    },\n" +
                                                            "    \"last\": true,\n" +
                                                            "    \"totalPages\": 1,\n" +
                                                            "    \"totalElements\": 4,\n" +
                                                            "    \"first\": true,\n" +
                                                            "    \"size\": 20,\n" +
                                                            "    \"number\": 0,\n" +
                                                            "    \"sort\": {\n" +
                                                            "        \"empty\": true,\n" +
                                                            "        \"sorted\": false,\n" +
                                                            "        \"unsorted\": true\n" +
                                                            "    },\n" +
                                                            "    \"numberOfElements\": 4,\n" +
                                                            "    \"empty\": false\n" +
                                                            "}"
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Listado de Solicitudes",
                                            content = @Content(schema = @Schema(implementation = Page.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/reporte/listadosolicitudes",
                    method = GET,
                    beanClass = HandlerSolicitudReporte.class,
                    beanMethod = "getReporteListadoSolicitudes",
                    operation = @Operation(
                            operationId = "getReporteListadoSolicitudes",
                            summary = "Reporte de Listado de Solicitudes",
                            description = "Devuelve lista de solicitudes hechas",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Texto devuelto exitosamente",
                                            content = @Content(schema = @Schema(implementation = String.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunctionReporte(HandlerSolicitudReporte handler) {
        return route(GET("/api/v1/reporte"), handler::getReporte)
        .and(route(GET("/api/v1/reporte/listadosolicitudes"), handler::getReporteListadoSolicitudes));
    }
}
