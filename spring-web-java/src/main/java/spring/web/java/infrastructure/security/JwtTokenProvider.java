package spring.web.java.infrastructure.security;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import spring.web.java.domain.model.enums.MemberRole;

public class JwtTokenProvider {

	private final SecretKey accessTokenKey;
	private final long accessTokenExpirationTime;
	private final SecretKey refreshTokenKey;
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

	public String createAccessToken(Long memberId, MemberRole role) {
		return Jwts.builder()
			.claim("memberId", memberId)
			.claim("role", role)
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

	public Claims parseRefreshToken(String token) {
		return Jwts.parser()
			.verifyWith(refreshTokenKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
}
