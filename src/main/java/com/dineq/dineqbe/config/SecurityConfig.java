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
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 추가
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/ws/**", // 웹소켓 핸드셰이크 허용
                                "/ws",

                                "/api/v1/auth/register", // 임시
                                "/api/v1/auth/check",
                                "/api/v1/menus",
                                "/api/v1/menus/**",
                                "/api/v1/orders",
                                "/api/v1/orders/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/v1/register/**",
                                "/QR.html",
                                "/QR-fail.html",
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

    // CORS 설정 추가
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // 쿠키 전송 허용
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://dineq.loseyourip.com",
                "https://dine-q-fe.vercel.app"
        )); // 프론트 주소

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
        config.setExposedHeaders(List.of("Set-Cookie")); // 쿠키 응답 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    /**
     * 쿠키를 크로스 사이트 요청에서도 사용할 수 있도록 SameSite=None; Secure 설정 추가
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("SESSION"); // 세션 쿠키 이름
        serializer.setCookiePath("/");
        
        // serializer.setUseSecureCookie(true); // 배포 시 이걸로 활성화, HTTPS에서만 쿠키 전송 (http에서는 쿠키 전송이 안되므로 개발 환경에서는 false로 설정)
        serializer.setUseSecureCookie(false); // localhost 로 테스트 시에는 이걸로 활성화
        serializer.setSameSite("None"); // 크로스 사이트 요청에서 쿠키 허용
        return serializer;
    }

}
