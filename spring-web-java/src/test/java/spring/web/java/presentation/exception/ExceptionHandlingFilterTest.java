package spring.web.java.presentation.exception;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import spring.web.java.presentation.exception.handler.ExceptionHandlingFilter;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlingFilterTest {

	@InjectMocks
	private ExceptionHandlingFilter exceptionHandlingFilter;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private FilterChain filterChain;

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@BeforeEach
	void setUp() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@Test
	@DisplayName("ExceptionHandlerFilter는 MalformedJwtException 발생할 경우 BAD_REQUEST 반환한다")
	void case1() throws ServletException, IOException {
		// Given
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "MalformedJwtException";
		String responseBody = """
			{
			  "status": %s,
			  "title": "%s",
			  "detail": "%s"
			}
			""".formatted(status.value(), status.getReasonPhrase(), message);

		Mockito.doThrow(new MalformedJwtException(message)).when(filterChain).doFilter(request, response);
		Mockito.when(objectMapper.writeValueAsString(Mockito.any(ProblemDetail.class))).thenReturn(responseBody);

		// When
		exceptionHandlingFilter.doFilter(request, response, filterChain);

		// Then
		Assertions.assertThat(response.getStatus()).isEqualTo(status.value());
		Assertions.assertThat(response.getContentAsString()).isEqualTo(responseBody);
	}

	@Test
	@DisplayName("ExceptionHandlerFilter는 JwtException 발생할 경우 UNAUTHORIZED 반환한다")
	void case2() throws ServletException, IOException {
		// Given
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		String message = "JwtException";
		String responseBody = """
			{
			  "status": %s,
			  "title": "%s",
			  "detail": "%s"
			}
			""".formatted(status.value(), status.getReasonPhrase(), message);

		Mockito.doThrow(new JwtException(message)).when(filterChain).doFilter(request, response);
		Mockito.when(objectMapper.writeValueAsString(Mockito.any(ProblemDetail.class))).thenReturn(responseBody);

		// When
		exceptionHandlingFilter.doFilter(request, response, filterChain);

		// Then
		Assertions.assertThat(response.getStatus()).isEqualTo(status.value());
		Assertions.assertThat(response.getContentAsString()).isEqualTo(responseBody);
	}

	@Test
	@DisplayName("ExceptionHandlerFilter는 RuntimeException 발생할 경우 INTERNAL_SERVER_ERROR 반환한다")
	void case3() throws ServletException, IOException {
		// Given
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String message = "RuntimeException";
		String responseBody = """
			{
			  "status": %s,
			  "title": "%s",
			  "detail": "%s"
			}
			""".formatted(status.value(), status.getReasonPhrase(), message);

		Mockito.doThrow(new RuntimeException(message)).when(filterChain).doFilter(request, response);
		Mockito.when(objectMapper.writeValueAsString(Mockito.any(ProblemDetail.class))).thenReturn(responseBody);

		// When
		exceptionHandlingFilter.doFilter(request, response, filterChain);

		// Then
		Assertions.assertThat(response.getStatus()).isEqualTo(status.value());
		Assertions.assertThat(response.getContentAsString()).isEqualTo(responseBody);
	}
}
