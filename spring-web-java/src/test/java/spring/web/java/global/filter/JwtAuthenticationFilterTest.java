package spring.web.java.global.filter;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import spring.web.java.global.common.JwtTokenProvider;

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
	void nullTokenTest() throws ServletException, IOException {
		// Given

		// When
		jwtAuthenticationFilter.doFilter(request, response, filterChain);

		// Then
		Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
	}

	@Test
	void emptyTokenTest() throws ServletException, IOException {
		// Given
		request.addHeader(HttpHeaders.AUTHORIZATION, "");

		// When
		jwtAuthenticationFilter.doFilter(request, response, filterChain);

		// Then
		Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
	}

	@Test
	void invalidTokenTest() {
		// Given
		String token = "invalid.jwt.token";

		Mockito.when(jwtTokenProvider.parseAccessToken(Mockito.any())).thenThrow(new JwtException("invalidToken"));

		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

		// When & Then
		Assertions.assertThatThrownBy(() -> jwtAuthenticationFilter.doFilter(request, response, filterChain))
			.isInstanceOf(JwtException.class);
	}

	@Test
	void validTokenTest() throws ServletException, IOException {
		// Given
		String token = "valid.jwt.token";
		Claims claims = Mockito.mock(Claims.class);

		Mockito.when(jwtTokenProvider.parseAccessToken(Mockito.any())).thenReturn(claims);

		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

		// When
		jwtAuthenticationFilter.doFilter(request, response, filterChain);

		// Then
		Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
		Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication().getCredentials()).isEqualTo(token);
	}
}
