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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF vì sử dụng token (JWT)
                .csrf(csrf -> csrf.disable())
                // Session stateless: vì xác thực được thực hiện qua token thay vì session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Cho phép truy cập công khai đến các endpoint đăng nhập/đăng ký của từng role:
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                        .requestMatchers("/api/shipper/register", "/api/shipper/login").permitAll()
                        .requestMatchers("/api/manager/register", "/api/manager/login").permitAll()
                        // Nếu có endpoint công khai khác:
                        .requestMatchers("/api/orders/**").permitAll()
                        // Các endpoint khác yêu cầu người dùng phải có role phù hợp:
                        .requestMatchers("/api/users/**").hasRole("USER")
                        .requestMatchers("/api/shipper/**").hasRole("SHIPPER")
                        .requestMatchers("/api/manager/**").hasRole("MANAGER")
                        // Các request khác phải được xác thực
                        .requestMatchers("/api/orders/statistics").hasRole("USER") // Chỉ USER truy cập thống kê
                        .requestMatchers("/api/orders/create-order").hasRole("USER") // Chỉ USER có thể tạo đơn
                        .requestMatchers("/api/orders/sent-orders").hasRole("USER")
                        .requestMatchers("/api/orders/assign-order").hasRole("MANAGER") // Chỉ MANAGER phân công đơn
                        .requestMatchers("/api/orders/deliver-order").hasRole("SHIPPER") // Chỉ SHIPPER cập nhật trạng thái giao hàng
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
}

