package spring.webmvc.infrastructure.security;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.FilterChain;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

	@Mock
	private JwtProvider jwtProvider;

	@Mock
	private FilterChain filterChain;

	private JwtAuthenticationFilter jwtAuthenticationFilter;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@BeforeEach
	void setUp() {
		jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProvider);
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("유효한 토큰이 있으면 인증 정보를 설정한다")
	void doFilterInternalWithValidToken() throws Exception {
		String token = "valid-token";
		Long userId = 1L;
		List<String> permissions = List.of("USER_READ");
		Claims claims = new DefaultClaims(Map.of(
			"userId", userId,
			"permissions", permissions
		));

		request.addHeader("Authorization", "Bearer " + token);
		when(jwtProvider.parseAccessToken(token)).thenReturn(claims);

		jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		assertThat(authentication).isNotNull();
		assertThat(authentication.getPrincipal()).isEqualTo(userId);
		assertThat(authentication.getCredentials()).isEqualTo(token);
		assertThat(authentication.getAuthorities()).hasSize(1);

		verify(filterChain).doFilter(request, response);
	}

	@Test
	@DisplayName("Authorization 헤더가 없으면 인증 정보를 설정하지 않는다")
	void doFilterInternalWithoutAuthorizationHeader() throws Exception {
		jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		assertThat(authentication).isNull();

		verify(filterChain).doFilter(request, response);
		verify(jwtProvider, never()).parseAccessToken(any());
	}

	@Test
	@DisplayName("Bearer 접두사가 없으면 인증 정보를 설정하지 않는다")
	void doFilterInternalWithoutBearerPrefix() throws Exception {
		request.addHeader("Authorization", "InvalidToken");

		jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		assertThat(authentication).isNull();

		verify(filterChain).doFilter(request, response);
		verify(jwtProvider, never()).parseAccessToken(any());
	}

	@Test
	@DisplayName("빈 토큰이면 인증 정보를 설정하지 않는다")
	void doFilterInternalWithEmptyToken() throws Exception {
		request.addHeader("Authorization", "Bearer ");

		jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

		verify(filterChain).doFilter(request, response);
	}
}
