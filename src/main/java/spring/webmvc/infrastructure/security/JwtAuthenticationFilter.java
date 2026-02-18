package spring.webmvc.infrastructure.security;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

	public JwtAuthenticationFilter(JwtProvider jwtProvider) {
		this.jwtProvider = jwtProvider;
	}

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		String token = extractToken(request);
		if (token != null) {
			Authentication authentication = createAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	private String extractToken(HttpServletRequest request) {
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authorization != null && authorization.startsWith("Bearer ")) {
			String token = authorization.substring(7).trim();
			return token.isEmpty() ? null : token;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Authentication createAuthentication(String token) {
		Claims claims = jwtProvider.parseAccessToken(token);

		Long userId = Long.valueOf(claims.get("userId").toString());
		List<String> permissions = claims.get("permissions", List.class);

		List<SimpleGrantedAuthority> authorities = permissions.stream()
			.map(SimpleGrantedAuthority::new)
			.toList();

		return new UsernamePasswordAuthenticationToken(userId, token, authorities);
	}
}
