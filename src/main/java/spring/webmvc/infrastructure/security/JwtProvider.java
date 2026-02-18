package spring.webmvc.infrastructure.security;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import spring.webmvc.infrastructure.properties.AppProperties;

@Component
public class JwtProvider {

	private final SecretKey accessTokenKey;
	private final long accessTokenExpirationTime;
	private final SecretKey refreshTokenKey;
	private final long refreshTokenExpirationTime;

	public JwtProvider(AppProperties appProperties) {
		this.accessTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(appProperties.jwt().accessToken().key()));
		this.accessTokenExpirationTime = appProperties.jwt().accessToken().expiration().toMillis();
		this.refreshTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(appProperties.jwt().refreshToken().key()));
		this.refreshTokenExpirationTime = appProperties.jwt().refreshToken().expiration().toMillis();
	}

	public String createAccessToken(Long userId, List<String> permissions) {
		return Jwts.builder()
			.claim("userId", userId)
			.claim("permissions", permissions)
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
			.signWith(accessTokenKey)
			.compact();
	}

	public String createRefreshToken() {
		return Jwts.builder()
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
			.signWith(refreshTokenKey)
			.compact();
	}

	public Claims parseAccessToken(String token) {
		return Jwts.parser()
			.verifyWith(accessTokenKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	public Claims parseRefreshToken(String token) {
		return Jwts.parser()
			.verifyWith(refreshTokenKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
}
