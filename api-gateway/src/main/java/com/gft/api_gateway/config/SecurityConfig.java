package com.gft.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(auth -> auth
                // ROTA PÚBLICA: Use /** para garantir que pegue sub-caminhos e barras finais
                .pathMatchers("/api/agendamento/cadastro/paciente/**").permitAll()

                // USER
                // Usamos hasAuthority para bater EXATAMENTE com o nome no Keycloak ("ROLE_USER")
                .pathMatchers(HttpMethod.PUT, "/api/agendamento/paciente/**").hasAuthority("ROLE_USER")
                .pathMatchers(HttpMethod.DELETE, "/api/agendamento/paciente/**").hasAuthority("ROLE_USER")
                .pathMatchers(HttpMethod.DELETE, "/api/procedimento/**").hasAuthority("ROLE_USER")
                
                .pathMatchers(
                    "/api/agendamento/paciente/cpf/**",
                    "/api/agendamento/consultas/**",
                    "/api/agendamento/cadastro/consulta/**",
                    "/api/agendamento/consulta/**",
                    "/api/agendamento/procedimentos/**",
                    "/api/agendamento/cadastro/procedimento/**",
                    "/api/agendamento/procedimento/**",
                    "/api/procedimento/cpf/**"
                ).hasAuthority("ROLE_USER")

                // MEDICO
                .pathMatchers(
                    "/api/agendamento/pacientes",
                    "/api/clinica/consultas",
                    "/api/clinica/cadastro/procedimento",
                    "/api/clinica/cadastro/doenca",
                    "/api/clinica/doencas",
                    "/api/clinica/doenca/**",
                    "/api/clinica/cadastro/doenca/**",
                    "/api/clinica/cadastro/sintoma",
                    "/api/clinica/sintomas",
                    "/api/clinica/sintoma/**",
                    "/api/clinica/sintomas/**",
                    "/api/clinica/cadastro/sintoma/**",
                    "/api/procedimento/procedimentos"
                ).hasAuthority("ROLE_MEDICO")

                // ADMIN
                .pathMatchers("/api/clinica/cadastro/medico/**").hasAuthority("ROLE_ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/api/clinica/medico/**").hasAuthority("ROLE_ADMIN")

                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(getJwtAuthenticationConverter()))
            );

        return http.build();
    }

    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> getJwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;
    }

    /**
     * Extrai as roles de realm_access.roles
     * Como suas roles já são "ROLE_USER", não adicionamos prefixo.
     */
    static class KeycloakRoleConverter implements Converter<Jwt, Flux<GrantedAuthority>> {
        @Override
        public Flux<GrantedAuthority> convert(Jwt jwt) {
            Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");

            System.out.println("DEBUG - Claims do Token: " + jwt.getClaims());

            if (realmAccess == null || realmAccess.isEmpty()) {
                System.out.println("DEBUG - Nenhuma role encontrada em realm_access");
                return Flux.empty();
            }

            Collection<String> roles = (Collection<String>) realmAccess.get("roles");
            System.out.println("DEBUG - Roles brutas encontradas: " + roles);
            
            return Flux.fromIterable(roles)
                .map(role -> {
                    // DEBUG: Verifique a autoridade final gerada
                    System.out.println("DEBUG - Authority gerada: " + role); 
                    return new SimpleGrantedAuthority(role);
                }); // Passa a string crua: "ROLE_USER"
        }
    }
}