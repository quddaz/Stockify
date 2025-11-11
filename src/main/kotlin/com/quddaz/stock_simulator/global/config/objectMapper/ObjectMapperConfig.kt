package com.quddaz.stock_simulator.global.config.objectMapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfig {

    @Bean
    fun yamlObjectMapper(): ObjectMapper {
        return ObjectMapper(YAMLFactory()).registerKotlinModule()
    }
}