package co.com.pragma.api.handler;

import co.com.pragma.api.request.SolicitudCreditoRequest;
import co.com.pragma.usecase.usuario.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class HandlerAutenticacion {

    private final UsuarioUseCase usuarioUseCase;
    private final Validator validator;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> holaMundo(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("Hola Mundo");
    }

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
                    return usuarioUseCase.guardarSolicitudCredito(
                                    userReq.getDocumentoCliente(),
                                    userReq.getMonto(),
                                    userReq.getPlazo(),
                                    userReq.getTipoPrestamo()
                            )
                            .flatMap(user -> ServerResponse.ok().bodyValue(user))
                            .onErrorResume(e ->
                                    ServerResponse.badRequest().bodyValue(e.getMessage())
                            );
                });
    }
}
