package com.quddaz.stock_simulator.global.response

data class ResponseTemplate<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val results: T? = null
) {
    companion object {
        val EMPTY_RESPONSE = ResponseTemplate(
            isSuccess = true,
            code = "REQUEST_OK",
            message = "요청이 승인되었습니다.",
            results = emptyMap<String, Any>()
        )

        val JSON_AUTH_ERROR = ResponseTemplate(
            isSuccess = false,
            code = "JSON_AUTH_ERROR",
            message = "로그인 후 다시 접근해주시기 바랍니다.",
            results = emptyMap<String, Any>()
        )

        val JSON_ROLE_ERROR = ResponseTemplate(
            isSuccess = false,
            code = "JSON_ROLE_ERROR",
            message = "가진 권한으로는 실행할 수 없는 기능입니다.",
            results = emptyMap<String, Any>()
        )

        fun <T> from(dto: T): ResponseTemplate<T> =
            ResponseTemplate(
                isSuccess = true,
                code = "REQUEST_OK",
                message = "request succeeded",
                results = dto
            )

        fun <T> fail(dto: T): ResponseTemplate<T> =
            ResponseTemplate(
                isSuccess = false,
                code = "Error",
                message = dto.toString(),
                results = null
            )
    }
}