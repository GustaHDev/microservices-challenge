package com.gft.agendamento_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {    

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            
            // 1. Garante que não guarde sessão (Stateless)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 2. Resolve o problema do "requestMatchers" não funcionar
            .authorizeHttpRequests(auth -> auth
                // Usamos "new AntPathRequestMatcher" para forçar a comparação via String simples da URL
                // Isso ignora a complexidade do DispatcherServlet
                .requestMatchers(new AntPathRequestMatcher("/api/agendamento/cadastro/paciente/**")).permitAll()
                
                // Regra padrão
                .anyRequest().authenticated()
            )
            
            // 3. Impede aquele log de "Saved request to session" e retorna 401 direto se falhar
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        
        return http.build();
    }
}