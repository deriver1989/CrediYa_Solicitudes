package co.com.pragma.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.HexFormat;

@Configuration
public class JwtDecoderConfig {

    @Bean
    public ReactiveJwtDecoder jwtDecoder(@Value("${app.jwt.secret}") String secretHex) {
        byte[] keyBytes = HexFormat.of().parseHex(secretHex);

        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("El secreto JWT debe tener al menos 32 bytes para HS256");
        }

        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");

        return NimbusReactiveJwtDecoder
                .withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}
