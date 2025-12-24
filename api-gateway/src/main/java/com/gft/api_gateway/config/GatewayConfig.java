package main.java.com.gft.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {

        return builder.routes()

            // Procedimentos criados via AGENDAMENTO
            .route("procedimento-agendamento", r -> r
                .path("/api/agendamento/**")
                .filters(f -> f.addRequestHeader(
                        "X-Request-Origin", "AGENDAMENTO"))
                .uri("http://procedimento-service:8083")
            )

            // Procedimentos criados via CLINICA
            .route("procedimento-clinica", r -> r
                .path("/api/clinica/**")
                .filters(f -> f.addRequestHeader(
                        "X-Request-Origin", "CLINICA"))
                .uri("http://procedimento-service:8083")
            )

            .build();
    }
}
