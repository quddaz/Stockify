package com.quddaz.stock_simulator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class StockSimulatorApplication

fun main(args: Array<String>) {
    runApplication<StockSimulatorApplication>(*args)
}
