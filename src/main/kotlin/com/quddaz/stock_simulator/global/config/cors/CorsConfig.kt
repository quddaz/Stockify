package com.quddaz.stock_simulator.global.config.cors

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource



@Configuration
class CorsConfig(
    @Value("\${cors.allowed.origins}")
    private val allowedOrigins: Array<String>
) {

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            // 자격 증명 포함 요청 허용
            allowCredentials = true

            // 허용할 Origin 목록 설정
            allowedOrigins = this@CorsConfig.allowedOrigins.toList()
            // 허용할 HTTP 메서드 지정
            allowedMethods = listOf(
                HttpMethod.POST.name(),
                HttpMethod.GET.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.OPTIONS.name()
            )
            // 허용할 요청 헤더 지정
            allowedHeaders = listOf("*")
            // 노출시킬 응답 헤더 지정
            exposedHeaders = listOf("Authorization", "Set-Cookie", "REFRESH_TOKEN")
        }

        val source = UrlBasedCorsConfigurationSource()

        // 모든 경로("/**")에 위의 config 설정을 적용
        source.registerCorsConfiguration("/**", config)
        return source
    }
}