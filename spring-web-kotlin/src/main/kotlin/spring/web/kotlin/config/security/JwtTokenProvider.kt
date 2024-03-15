package spring.web.kotlin.config.security

import io.jsonwebtoken.JwsHeader
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.security.Key
import java.util.*

class JwtTokenProvider private constructor(
    private val accessTokenKey: Key,
    private val accessTokenExpirationTime: Long,
    private val refreshTokenKey: Key,
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

    fun createAccessToken(account: String, authorities: String) = Jwts.builder()
        .setHeaderParam(JwsHeader.ALGORITHM, SignatureAlgorithm.HS256)
        .setHeaderParam(JwsHeader.TYPE, JwsHeader.JWT_TYPE)
        .claim("account", account)
        .claim("authorities", authorities)
        .setIssuedAt(Date())
        .setExpiration(Date(Date().time + accessTokenExpirationTime))
        .signWith(accessTokenKey, SignatureAlgorithm.HS256)
        .compact()

    fun createRefreshToken() = Jwts.builder()
        .setHeaderParam(JwsHeader.ALGORITHM, SignatureAlgorithm.HS256)
        .setHeaderParam(JwsHeader.TYPE, JwsHeader.JWT_TYPE)
        .setIssuedAt(Date())
        .setExpiration(Date(Date().time + refreshTokenExpirationTime))
        .signWith(refreshTokenKey, SignatureAlgorithm.HS256)
        .compact()

    fun parseAccessToken(token: String) = Jwts.parserBuilder()
        .setSigningKey(accessTokenKey)
        .build()
        .parseClaimsJws(token)
        .body
}