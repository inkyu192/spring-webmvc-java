package spring.webmvc.infrastructure.config.security;

import java.io.IOException;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

	@InjectMocks
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@Mock
	private FilterChain filterChain;

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@BeforeEach
	void setUp() {
		SecurityContextHolder.clearContext();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@Test
	@DisplayName("JwtAuthenticationFilter 는 토큰이 null 일 경우 authentication 을 생성하지 않는다")
	void case1() throws ServletException, IOException {
		// Given

		// When
		jwtAuthenticationFilter.doFilter(request, response, filterChain);

		// Then
		Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
	}

	@Test
	@DisplayName("JwtAuthenticationFilter 는 토큰이 비어있을 경우 authentication 을 생성하지 않는다")
	void case2() throws ServletException, IOException {
		// Given
		request.addHeader(HttpHeaders.AUTHORIZATION, "");

		// When
		jwtAuthenticationFilter.doFilter(request, response, filterChain);

		// Then
		Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
	}

	@Test
	@DisplayName("JwtAuthenticationFilter 는 잘못된 토큰일 경우 JwtException 발생한다")
	void case3() {
		// Given
		String token = "invalid.jwt.token";

		Mockito.when(jwtTokenProvider.parseAccessToken(token)).thenThrow(new JwtException("invalidToken"));

		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

		// When & Then
		Assertions.assertThatThrownBy(() -> jwtAuthenticationFilter.doFilter(request, response, filterChain))
			.isInstanceOf(JwtException.class);
	}

	@Test
	@DisplayName("JwtAuthenticationFilter 는 유효한 토큰일 경우 authentication 을 생성 한다")
	void case4() throws ServletException, IOException {
		// Given
		String token = "valid.jwt.token";
		Claims claims = Mockito.mock(Claims.class);
		Long memberId = 1L;
		List<String> permissions = List.of("ITEM_READ");

		Mockito.when(jwtTokenProvider.parseAccessToken(token)).thenReturn(claims);
		Mockito.when(claims.get("memberId", Long.class)).thenReturn(memberId);
		Mockito.when(claims.get("permissions", List.class)).thenReturn(permissions);

		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

		// When
		jwtAuthenticationFilter.doFilter(request, response, filterChain);

		// Then
		Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
		Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication().getCredentials()).isEqualTo(token);
	}
}
