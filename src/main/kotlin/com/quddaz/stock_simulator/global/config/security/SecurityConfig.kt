package com.quddaz.stock_simulator.global.config.security

import com.quddaz.stock_simulator.domain.oauth.service.CustomOAuth2UserService
import com.quddaz.stock_simulator.global.security.filter.JwtAuthenticationFilter
import com.quddaz.stock_simulator.global.security.handler.CustomOAuth2SuccessHandler
import com.quddaz.stock_simulator.global.security.handler.JwtAccessDeniedHandler
import com.quddaz.stock_simulator.global.security.handler.JwtAuthenticationEntryPoint
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val customOAuth2SuccessHandler: CustomOAuth2SuccessHandler,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler
) {

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
            .formLogin { it.disable() } // 기본 로그인 페이지 사용 안함
            .httpBasic { it.disable() } // HTTP Basic 인증 사용 안함
            .cors(Customizer.withDefaults()) // 기본 CORS 설정 사용
            .csrf { it.disable() } // CSRF 보호 비활성화
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler)
            }
            .oauth2Login { oauth2 ->
                oauth2
                    .successHandler(customOAuth2SuccessHandler) // OAuth2 로그인 성공 후 처리
                    .userInfoEndpoint { userInfo ->
                        userInfo.userService(customOAuth2UserService) // OAuth2UserService 설정
                    }
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