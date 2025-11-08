package com.quddaz.stock_simulator.domain.oauth.exception.errorcode

import com.quddaz.stock_simulator.global.exception.errorcode.ErrorCode
import org.springframework.http.HttpStatus

enum class AuthErrorCode(
    override val httpStatus: HttpStatus,
    override val message: String
) : ErrorCode {
    INVALID_OAUTH_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid OAuth token."),
    UNSUPPORTED_OAUTH_PROVIDER(HttpStatus.BAD_REQUEST, "Unsupported OAuth provider."),
    NOT_EXISTING_OAUTH_TOKEN(HttpStatus.UNAUTHORIZED, "OAuth token does not exist.")
}