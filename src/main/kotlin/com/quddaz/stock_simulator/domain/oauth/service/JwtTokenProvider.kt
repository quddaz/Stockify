package com.quddaz.stock_simulator.domain.oauth.service

import com.quddaz.stock_simulator.domain.user.entity.Role
import com.quddaz.stock_simulator.domain.user.entity.User
import com.quddaz.stock_simulator.domain.user.service.UserService
import com.quddaz.stock_simulator.global.config.jwt.JwtProperties
import com.quddaz.stock_simulator.global.log.Loggable
import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.io.Encoders
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.Cookie
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*


@Component
class JwtTokenProvider(
    private val jwtProperties: JwtProperties,
    private val userService: UserService

) : Loggable {
    private lateinit var key: Key

    companion object {
        private const val MEMBER_ROLE = "role"
    }

    @PostConstruct
    fun setKey() {
        val keyBytes = Decoders.BASE64.decode(jwtProperties.secretKey)
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }

    /** AccessToken 생성 */
    fun createAccessToken(userId: Long, role: Role): String {
        val now = Date().time
        val accessValidity = Date(now + jwtProperties.accessTokenExpiration)

        return Jwts.builder()
            .setIssuedAt(Date(now))
            .setExpiration(accessValidity)
            .setIssuer(jwtProperties.issuer)
            .setSubject(userId.toString())
            .claim(MEMBER_ROLE, role.toString())
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    /** RefreshToken 생성 */
    fun createRefreshToken(userId: Long, role: Role): Cookie {
        val now = Date().time
        val refreshValidity = Date(now + jwtProperties.refreshTokenExpiration)

        val refreshToken = Jwts.builder()
            .setIssuedAt(Date(now))
            .setExpiration(refreshValidity)
            .setIssuer(jwtProperties.issuer)
            .setSubject(userId.toString())
            .claim(MEMBER_ROLE, role.toString())
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()

        return createCookie(refreshToken)
    }

    /** 토큰 유효성 검사 */
    fun validateToken(token: String): Boolean {
        return try {
            log.info("now date: {}", Date())
            val claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)

            claims.body.expiration.after(Date())
        } catch (e: Exception) {
            log.error("Token validation error", e)
            false
        }
    }

    /**
     * 토큰에서 User 정보 조회
     */
    fun getUser(token: String): User {
        val id = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
            .toLong()

        log.info("in getMember() socialId: $id")

        return userService.findById(id)
    }

    /**
     * 일반 Cookie 생성
     */
    fun createCookie(refreshToken: String): Cookie {
        val cookie = Cookie("REFRESH_TOKEN", refreshToken)
        cookie.maxAge = (jwtProperties.refreshTokenExpiration / 1000).toInt()
        cookie.path = "/"
        cookie.secure = true // HTTPS에서만 전송
        cookie.isHttpOnly = true // JavaScript에서 접근 불가
        return cookie
    }
}