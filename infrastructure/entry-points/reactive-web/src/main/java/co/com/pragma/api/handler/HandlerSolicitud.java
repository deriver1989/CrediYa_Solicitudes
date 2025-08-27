package co.com.pragma.api.handler;

import co.com.pragma.api.request.SolicitudCreditoRequest;
import co.com.pragma.model.solicitud.SolicitudCredito;
import co.com.pragma.usecase.solicitud.SolicitudUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HandlerSolicitud {

    private final SolicitudUseCase solicitudUseCase;
    private final Validator validator;

    public Mono<ServerResponse> guardarSolicitudCredito(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(SolicitudCreditoRequest.class)
                .flatMap(userReq -> {
                    // Validar el objeto
                    BeanPropertyBindingResult errors = new BeanPropertyBindingResult(userReq, SolicitudCreditoRequest.class.getName());
                    validator.validate(userReq, errors);

                    if (errors.hasErrors()) {
                        // devolver errores en formato JSON
                        return ServerResponse.badRequest().bodyValue(
                                errors.getFieldErrors().stream()
                                        .collect(java.util.stream.Collectors.toMap(
                                                e -> e.getField(),
                                                e -> e.getDefaultMessage()
                                        ))
                        );
                    }

                    // si pasa validación → usar el caso de uso
                    return solicitudUseCase.guardarSolicitudCredito(mapToUsuario(userReq))
                            .flatMap(user -> ServerResponse.ok().bodyValue(user))
                            .onErrorResume(e -> {
                                        return ServerResponse.badRequest().bodyValue(generarJsonMsg("Error al guardar la solicitud de crédito.",e.getMessage()));
                                    }
                            );
                })
                .onErrorResume(e -> {
                    return ServerResponse.badRequest().bodyValue(generarJsonMsg("Error al guardar la solicitud de crédito.",""));
                });
    }

    private Map<String, Object> generarJsonMsg(String error, String detalle){
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", error);
        errorResponse.put("detalle",detalle);
        return errorResponse;
    }

    private SolicitudCredito mapToUsuario(SolicitudCreditoRequest request) {
        return new SolicitudCredito(
                request.getDocumentoCliente(),
                request.getPlazo(),
                request.getMonto(),
                request.getTipoPrestamo()
        );
    }

}
