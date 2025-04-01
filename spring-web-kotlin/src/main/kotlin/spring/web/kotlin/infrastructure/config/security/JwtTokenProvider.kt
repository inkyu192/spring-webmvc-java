package spring.web.kotlin.infrastructure.config.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.access-token.key}") accessTokenKey: String,
    @Value("\${jwt.access-token.expiration-duration}") accessTokenExpirationDuration: Duration,
    @Value("\${jwt.refresh-token.key}") refreshTokenKey: String,
    @Value("\${jwt.refresh-token.expiration-duration}") refreshTokenExpirationDuration: Duration,
) {
    private val accessTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenKey))
    private val accessTokenExpirationTime = accessTokenExpirationDuration.toMillis()
    private val refreshTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshTokenKey))
    private val refreshTokenExpirationTime = refreshTokenExpirationDuration.toMillis()

    fun createAccessToken(memberId: Long, permissions: List<String>): String =
        Jwts.builder()
            .claim("memberId", memberId)
            .claim("permissions", permissions)
            .issuedAt(Date())
            .expiration(Date(Date().time + accessTokenExpirationTime))
            .signWith(accessTokenKey)
            .compact()

    fun createRefreshToken(): String =
        Jwts.builder()
            .issuedAt(Date())
            .expiration(Date(Date().time + refreshTokenExpirationTime))
            .signWith(refreshTokenKey)
            .compact()

    fun parseAccessToken(token: String): Claims =
        Jwts.parser()
            .verifyWith(accessTokenKey)
            .build()
            .parseSignedClaims(token)
            .payload

    fun parseRefreshToken(token: String): Claims =
        Jwts.parser()
            .verifyWith(refreshTokenKey)
            .build()
            .parseSignedClaims(token)
            .payload
}