package com.quddaz.stock_simulator.global.util

import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Transactional
@Component
annotation class BaseDataInit()
