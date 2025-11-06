package com.quddaz.stock_simulator.global.response

data class ResponseTemplate<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val results: T? = null
) {
    companion object {
        fun <T> success(
            results: T,
            code: String = "REQUEST_OK",
            message: String = "요청이 성공적으로 처리되었습니다."
        ): ResponseTemplate<T> =
            ResponseTemplate(
                isSuccess = true,
                code = code,
                message = message,
                results = results
            )

        fun fail(
            message: String,
            code: String = "ERROR",
            results: Any? = null
        ): ResponseTemplate<Any> =
            ResponseTemplate(
                isSuccess = false,
                code = code,
                message = message,
                results = results
            )
    }
}