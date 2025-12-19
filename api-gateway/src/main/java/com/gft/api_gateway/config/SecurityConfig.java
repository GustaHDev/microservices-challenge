package com.gft.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth

                        .pathMatchers("/api/clinica/cadastro/paciente").permitAll()

                        // USER
                        .pathMatchers(HttpMethod.PUT, "/api/agendamento/paciente/**")
                            .hasRole("ROLE_USER")

                        .pathMatchers(HttpMethod.DELETE, "/api/agendamento/paciente/**")
                            .hasRole("ROLE_USER")
                            
                        .pathMatchers(HttpMethod.DELETE, "/api/procedimento/**")
                            .hasRole("ROLE_USER")

                        .pathMatchers("/api/agendamento/paciente/cpf/**",

                                "/api/agendamento/consultas/**",
                                "/api/agendamento/cadastro/consulta/**",
                                "/api/agendamento/consulta/**",
                            
                                "/api/agendamento/procedimentos/**",
                                "/api/agendamento/cadastro/procedimento/**",
                                "/api/agendamento/procedimento/**",
                            
                                "/api/procedimento/cpf/**")
                            .hasRole("ROLE_USER")

                        // MEDICO
                        .pathMatchers("/api/agendamento/pacientes", 
                                     "/api/clinica/consultas",
                                     "/api/clinica/cadastro/procedimento",
                                    
                                    "/api/clinica/cadastro/doenca",
                                    "/api/clinica/doencas",
                                    "/api/clinica/doenca/**",
                                    "/api/clinica/doenca/**/sintomas",
                                    "/api/clinica/cadastro/doenca/**",
                                
                                    "/api/clinica/cadastro/sintoma",
                                    "/api/clinica/sintomas",
                                    "/api/clinica/sintoma/**",
                                    "/api/clinica/sintomas/**",
                                    "/api/clinica/sintoma/**/doencas",
                                    "/api/clinica/cadastro/sintoma/**",
                                
                                    "/api/procedimento/procedimentos")
                        .hasRole("ROLE_MEDICO")

                        .pathMatchers("/api/clinica/cadastro/medico",
                                     "/api/clinica/cadastro/medico/**"
                        )
                            .hasRole("ROLE_ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/clinica/medico/**")
                            .hasRole("ROLE_ADMIN")

                        .anyExchange().hasRole("ROLE_ADMIN"))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("realm_access.roles");

        JwtAuthenticationConverter authenticationConverter =
            new JwtAuthenticationConverter();

        authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return authenticationConverter;
}


}
