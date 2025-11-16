package com.quddaz.stock_simulator.domain.oauth

import com.quddaz.stock_simulator.domain.oauth.exception.TokenNotValidException
import com.quddaz.stock_simulator.domain.oauth.exception.errorcode.AuthErrorCode
import com.quddaz.stock_simulator.domain.oauth.service.AuthService
import com.quddaz.stock_simulator.domain.oauth.service.JwtTokenProvider
import com.quddaz.stock_simulator.domain.user.entity.Role
import com.quddaz.stock_simulator.domain.user.entity.SocialType
import com.quddaz.stock_simulator.domain.user.entity.User
import jakarta.servlet.http.Cookie
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
@SpringBootTest
class AuthServiceTest {
    @Mock
    private lateinit var jwtTokenProvider: JwtTokenProvider

    private lateinit var authService: AuthService

    @BeforeEach
    fun setUp() {
        authService = AuthService(jwtTokenProvider)
    }

    @Test
    fun `reissueAccessToken 테스트 RefreshToken을 받아 새로운 검증 토큰 리턴`() {
        // given
        val refreshToken = "validRefreshToken"
        val user = User(
            socialId = "socialId",
            email = "email",
            name = "name",
            socialType = SocialType.GOOGLE,
            role = Role.USER
        )
        user.id = 1L

        val cookie = Cookie("REFRESH_TOKEN", "newRefreshToken")

        // mocking
        whenever(jwtTokenProvider.validateToken(refreshToken)).thenReturn(true)
        whenever(jwtTokenProvider.getUser(refreshToken)).thenReturn(user)
        whenever(jwtTokenProvider.createAccessToken(1L, Role.USER)).thenReturn("newAccessToken")
        whenever(jwtTokenProvider.createRefreshToken(1L, Role.USER)).thenReturn(cookie)

        // when
        val result = authService.reissueAccessToken(refreshToken)

        // then
        assertEquals("newAccessToken", result.accessToken)
        assertEquals("newRefreshToken", result.refreshToken.value)
    }

    @Test
    fun `reissueAccessToken 테스트 RefreshToken이 유효하지 않을 경우`() {
        // given
        val invalidToken = "invalidToken"

        whenever(jwtTokenProvider.validateToken(invalidToken)).thenReturn(false)

        // when
        val exception = assertThrows<TokenNotValidException> {
            authService.reissueAccessToken(invalidToken)
        }

        // then
        assertEquals(AuthErrorCode.INVALID_OAUTH_TOKEN, exception.errorCode)
    }

    @Test
    fun `reissueAccessToken 테스트 RefreshToken이 null일 경우`() {
        val exception = assertThrows<TokenNotValidException> {
            authService.reissueAccessToken(null)
        }
        assertEquals(AuthErrorCode.NOT_EXISTING_OAUTH_TOKEN, exception.errorCode)
    }
}