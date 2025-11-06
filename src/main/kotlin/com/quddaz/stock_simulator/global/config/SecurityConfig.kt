package com.quddaz.stock_simulator.global.config

import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig {

    private val WHITE_LIST = arrayOf(
        "/error",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/swagger-resources/*",
        "/webjars/**",
        "/global/**",
        "/actuator/**",
        "/auth/**"
    )

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .formLogin { obj: FormLoginConfigurer<HttpSecurity> -> obj.disable() } // 기본 시큐리티 로그인 페이지 사용 안함
            .httpBasic { obj: HttpBasicConfigurer<HttpSecurity> -> obj.disable() } // HTTP 기본 인증 사용 안함
            .cors(Customizer.withDefaults()) // CORS 설정 -> 기본 corsConfigurationSource 빈 사용
            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() } // CSRF 보호 기능 비활성화
            .sessionManagement { sessionManagement: SessionManagementConfigurer<HttpSecurity?> ->
                sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(*WHITE_LIST).permitAll()
                    .anyRequest().authenticated()
            }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}