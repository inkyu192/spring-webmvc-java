package spring.web.java.infrastructure.security;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String token = extractToken(request);

		if (StringUtils.hasText(token)) {
			SecurityContextHolder.getContext().setAuthentication(generateAuthentication(token));
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

	private Authentication generateAuthentication(String token) {
		Claims claims = jwtTokenProvider.parseAccessToken(token);

		return new UsernamePasswordAuthenticationToken(
			claims.get("memberId"),
			token,
			List.of(new SimpleGrantedAuthority(String.valueOf(claims.get("role"))))
		);
	}
}
