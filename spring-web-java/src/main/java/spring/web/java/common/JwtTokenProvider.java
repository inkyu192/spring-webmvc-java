package spring.web.java.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import spring.web.java.constant.Role;

import java.security.Key;
import java.util.Date;

public class JwtTokenProvider {

    private final Key accessTokenKey;
    private final long accessTokenExpirationTime;
    private final Key refreshTokenKey;
    private final long refreshTokenExpirationTime;

    public JwtTokenProvider(
            String accessTokenKey,
            long accessTokenExpirationTime,
            String refreshTokenKey,
            long refreshTokenExpirationTime
    ) {
        this.accessTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenKey));
        this.accessTokenExpirationTime = accessTokenExpirationTime * 60 * 1000;
        this.refreshTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshTokenKey));
        this.refreshTokenExpirationTime = refreshTokenExpirationTime * 60 * 1000;
    }

    public String createAccessToken(Long memberId, Role role) {
        return Jwts.builder()
                .setHeaderParam(JwsHeader.ALGORITHM, SignatureAlgorithm.HS256)
                .setHeaderParam(JwsHeader.TYPE, JwsHeader.JWT_TYPE)
                .claim("memberId", memberId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + accessTokenExpirationTime))
                .signWith(accessTokenKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken() {
        return Jwts.builder()
                .setHeaderParam(JwsHeader.ALGORITHM, SignatureAlgorithm.HS256)
                .setHeaderParam(JwsHeader.TYPE, JwsHeader.JWT_TYPE)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + refreshTokenExpirationTime))
                .signWith(refreshTokenKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(accessTokenKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims parseRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshTokenKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
