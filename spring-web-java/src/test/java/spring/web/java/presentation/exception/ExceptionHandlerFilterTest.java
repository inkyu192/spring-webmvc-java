package spring.web.java.presentation.exception;

import java.io.IOException;

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

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import spring.web.java.infrastructure.util.ResponseWriter;
import spring.web.java.presentation.exception.handler.ExceptionHandlerFilter;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerFilterTest {

	@InjectMocks
	private ExceptionHandlerFilter exceptionHandlerFilter;

	@Mock
	private FilterChain filterChain;

	@Mock
	private ResponseWriter responseWriter;

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@BeforeEach
	void setUp() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@Test
	@DisplayName("ExceptionHandlerFilter 는 JwtException 발생할 경우 UNAUTHORIZED 반환한다")
	void case1() throws ServletException, IOException {
		// Given
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		String message = "JwtException";

		Mockito.doThrow(new JwtException(message)).when(filterChain).doFilter(request, response);

		// When
		exceptionHandlerFilter.doFilter(request, response, filterChain);

		// Then
		Mockito.verify(responseWriter).writeResponse(ProblemDetail.forStatusAndDetail(status, message));
	}

	@Test
	@DisplayName("ExceptionHandlerFilter 는 RuntimeException 발생할 경우 INTERNAL_SERVER_ERROR 반환한다")
	void case2() throws ServletException, IOException {
		// Given
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String message = "RuntimeException";

		Mockito.doThrow(new RuntimeException(message)).when(filterChain).doFilter(request, response);

		// When
		exceptionHandlerFilter.doFilter(request, response, filterChain);

		// Then
		Mockito.verify(responseWriter).writeResponse(ProblemDetail.forStatusAndDetail(status, message));
	}
}
