package spring.webmvc.infrastructure.config.security;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

	private final SecretKey accessTokenKey;
	private final long accessTokenExpirationTime;
	private final SecretKey refreshTokenKey;
	private final long refreshTokenExpirationTime;

	public JwtProvider(JwtProperties jwtProperties) {
		accessTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getAccessToken().getKey()));
		accessTokenExpirationTime = jwtProperties.getAccessToken().getExpiration().toMillis();
		refreshTokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getRefreshToken().getKey()));
		refreshTokenExpirationTime = jwtProperties.getRefreshToken().getExpiration().toMillis();
	}

	public String createAccessToken(Long memberId, List<String> permissions) {
		return Jwts.builder()
			.claim("memberId", memberId)
			.claim("permissions", permissions)
			.issuedAt(new Date())
			.expiration(new Date(new Date().getTime() + accessTokenExpirationTime))
			.signWith(accessTokenKey)
			.compact();
	}

	public String createRefreshToken() {
		return Jwts.builder()
			.issuedAt(new Date())
			.expiration(new Date(new Date().getTime() + refreshTokenExpirationTime))
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

	public void validateRefreshToken(String token) {
		Jwts.parser()
			.verifyWith(refreshTokenKey)
			.build()
			.parseSignedClaims(token);
	}
}
