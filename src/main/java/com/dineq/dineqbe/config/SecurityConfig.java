package com.dineq.dineqbe.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.dineq.dineqbe.security.CustomAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/register",
                                "/api/v1/auth/register/admin",
                                "/api/v1/menus",
                                "/api/v1/menus/**",
                                "/api/v1/orders",
                                "/api/v1/orders/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "api/v1/register/**",
                                "/qrcode-test.html",                   // 테스트용 나중에 지울 것
                                "/css/**", "/js/**", "/images/**"  // 테스트용 나중에 지울 것
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginProcessingUrl("/api/v1/auth/login") // POST 요청 시 로그인
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/api/v1/logout") // POST 요청 시 로그아웃
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // 세션이 없는 경우
                            if (authentication == null) {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                                return;
                            }

                            // 세션이 있는 경우
                            response.setStatus(HttpServletResponse.SC_OK); // 200
                        })
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, authException) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다."))
                )

                .csrf(AbstractHttpConfigurer::disable)  // CSRF 비활성화
                .httpBasic(AbstractHttpConfigurer::disable); // 기본 인증 비활성화

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
