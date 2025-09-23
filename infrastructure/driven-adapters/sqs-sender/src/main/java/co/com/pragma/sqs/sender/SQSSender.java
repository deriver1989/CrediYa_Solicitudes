package co.com.pragma.sqs.sender;

import co.com.pragma.model.solicitud.gateways.ClienteSQSRepository;
import co.com.pragma.sqs.sender.config.SQSSenderProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements ClienteSQSRepository {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;

    public Mono<String> sendMessage (String message) {//public Mono<String> send(String message) {
        return Mono.fromCallable(() -> buildRequest(message))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequest(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(message)
                .build();
    }

    @Override
    public void send(String messageBody) {
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(messageBody)
                .build();

        Mono.fromFuture( client.sendMessage(request))
                .doOnSuccess(response -> log.info("✅ Mensaje enviado con ID: "))
                .doOnError(e ->{
                    System.err.println("❌ Error al enviar mensaje: " + e.getMessage());
                    log.error("❌ Error al enviar mensaje: " + e.getMessage());
                })
                .subscribe();
    }
}
