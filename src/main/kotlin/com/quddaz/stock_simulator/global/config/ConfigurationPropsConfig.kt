package com.quddaz.stock_simulator.global.config

import com.quddaz.stock_simulator.global.config.jwt.JwtProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class ConfigurationPropsConfig {
}