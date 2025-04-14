package spring.webmvc.infrastructure.config.security;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String token = extractToken(request);

		if (StringUtils.hasText(token)) {
			SecurityContextHolder.getContext().setAuthentication(createAuthentication(token));
		}

		filterChain.doFilter(request, response);
	}

	private String extractToken(HttpServletRequest request) {
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer")) {
			return authorization.replace("Bearer ", "");
		}

		return null;
	}

	private Authentication createAuthentication(String token) {
		Claims claims = jwtProvider.parseAccessToken(token);

		Long memberId = claims.get("memberId", Long.class);
		List<?> permissions = claims.get("permissions", List.class);

		List<SimpleGrantedAuthority> authorities = permissions.stream()
			.map(String::valueOf)
			.map(SimpleGrantedAuthority::new)
			.toList();

		return new UsernamePasswordAuthenticationToken(memberId, token, authorities);
	}
}
