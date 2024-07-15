package com.homelyassist.security;

import com.homelyassist.service.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile("local")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/assist/availability").permitAll()
                        .requestMatchers("/assist").permitAll()
                        .requestMatchers("/assist/search").permitAll()
                        .requestMatchers("/assist/agriculture/search").permitAll()
                        .requestMatchers("/assist/electrical/search").permitAll()
                        .requestMatchers("/assist/construction/search").permitAll()
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers("/api/auth/validate").authenticated()
                        .requestMatchers("/api/assist/agriculture/search").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/assist/register").permitAll()
                        .requestMatchers("/assist/login").permitAll()
                        .requestMatchers("/assist/forgot-password").permitAll()
                        .requestMatchers("/help").permitAll()
                        .requestMatchers("/team").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }
}
