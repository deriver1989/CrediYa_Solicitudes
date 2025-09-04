package co.com.pragma.api.handler;

import co.com.pragma.api.mensaje.Mensaje;
import co.com.pragma.api.request.SolicitudCreditoRequest;
import co.com.pragma.model.solicitud.SolicitudCredito;
import co.com.pragma.usecase.solicitud.SolicitudUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
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
                    return ReactiveSecurityContextHolder.getContext()
                            .flatMap(ctx -> {
                                Jwt jwt = (Jwt) ctx.getAuthentication().getPrincipal();
                                String username = jwt.getClaimAsString("sub"); //obtener username
                                List<String> roles = List.of(jwt.getClaimAsString("roles").split(","));

                                return solicitudUseCase.guardarSolicitudCredito(mapToUsuario(userReq, username, roles))
                                        .flatMap(user -> ServerResponse.status(HttpStatus.CREATED).bodyValue(user))
                                        .onErrorResume(e -> {
                                                    return ServerResponse.badRequest().bodyValue(generarJsonMsg(Mensaje.ERROR_GUARDAR_SOLICITUD, e.getMessage()));
                                                }
                                        );
                            });
                })
                .onErrorResume(e -> {
                    return ServerResponse.badRequest().bodyValue(generarJsonMsg(Mensaje.ERROR_GUARDAR_SOLICITUD,""));
                });
    }

    private Map<String, Object> generarJsonMsg(String error, String detalle){
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", error);
        errorResponse.put("detalle",detalle);
        return errorResponse;
    }

    private SolicitudCredito mapToUsuario(SolicitudCreditoRequest request, String username , List<String> roles) {
        SolicitudCredito nuevo = new SolicitudCredito();
        nuevo.setDocumentoCliente(request.getDocumentoCliente());
        nuevo.setPlazo(request.getPlazo());
        nuevo.setMonto(request.getMonto());
        nuevo.setTipoPrestamo(request.getTipoPrestamo());
        nuevo.setUsername(username);
        nuevo.setRoles(roles);
        return nuevo;
    }

}
