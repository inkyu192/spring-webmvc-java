package spring.web.kotlin.global.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import spring.web.kotlin.domain.member.Member
import java.util.*
import javax.crypto.SecretKey

class JwtTokenProvider(
    private val accessTokenKey: SecretKey,
    private val accessTokenExpirationTime: Long,
    private val refreshTokenKey: SecretKey,
    private val refreshTokenExpirationTime: Long
) {
    constructor(
        accessTokenKey: String,
        accessTokenExpirationTime: Long,
        refreshTokenKey: String,
        refreshTokenExpirationTime: Long
    ) : this(
        accessTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenKey)),
        accessTokenExpirationTime = accessTokenExpirationTime * 60 * 1000,
        refreshTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshTokenKey)),
        refreshTokenExpirationTime = refreshTokenExpirationTime * 60 * 1000
    )

    fun createAccessToken(memberId: Long, role: Member.Role): String =
        Jwts.builder()
            .claim("memberId", memberId)
            .claim("role", role)
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