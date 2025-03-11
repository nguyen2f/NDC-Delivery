package com.ndc.deliverymanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Thêm cấu hình CORS
                // Disable CSRF vì sử dụng token (JWT)
                .csrf(csrf -> csrf.disable())
                // Session stateless: vì xác thực được thực hiện qua token thay vì session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                        .requestMatchers("/api/shipper/register", "/api/shipper/login").permitAll()
                        .requestMatchers("/api/manager/register", "/api/manager/login").permitAll()
                        .requestMatchers("/api/orders/**").permitAll()
                        .requestMatchers("/api/users/**").hasRole("USER")
                        .requestMatchers("/api/shipper/**").hasRole("SHIPPER")
                        .requestMatchers("/api/manager/**").hasRole("MANAGER")
                        .requestMatchers("/api/orders/statistics").hasRole("USER")
                        .requestMatchers("/api/orders/create-order").hasRole("USER")
                        .requestMatchers("/api/orders/sent-orders").hasRole("USER")
                        .requestMatchers("/api/orders/assign-order").hasRole("MANAGER")
                        .requestMatchers("/api/orders/deliver-order").hasRole("SHIPPER")
                        .requestMatchers("/api/payment/**").permitAll()
                        .anyRequest().authenticated()
                );

        // Nếu sử dụng JWT, thêm JwtFilter vào chuỗi filter:
        // http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        // Các người dùng mẫu cho mục đích test:
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();

        UserDetails shipper = User.withUsername("shipper")
                .password(passwordEncoder().encode("password"))
                .roles("SHIPPER")
                .build();

        UserDetails manager = User.withUsername("manager")
                .password(passwordEncoder().encode("password"))
                .roles("MANAGER")
                .build();

        return new InMemoryUserDetailsManager(user, shipper, manager);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Cho phép Angular gọi API
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization")); // Để Angular có thể nhận JWT token
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

