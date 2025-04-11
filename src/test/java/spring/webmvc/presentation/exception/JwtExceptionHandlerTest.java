package spring.webmvc.presentation.exception;

import java.io.IOException;
import java.net.URI;

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
import spring.webmvc.infrastructure.util.ProblemDetailUtil;
import spring.webmvc.infrastructure.util.ResponseWriter;
import spring.webmvc.presentation.exception.handler.JwtExceptionHandler;

@ExtendWith(MockitoExtension.class)
class JwtExceptionHandlerTest {

	@InjectMocks
	private JwtExceptionHandler jwtExceptionHandler;

	@Mock
	private FilterChain filterChain;

	@Mock
	private ProblemDetailUtil problemDetailUtil;

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
		URI uri = URI.create("uri");

		Mockito.doThrow(new JwtException(message)).when(filterChain).doFilter(request, response);
		Mockito.when(problemDetailUtil.createType(status)).thenReturn(uri);

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
		problemDetail.setType(uri);

		// When
		jwtExceptionHandler.doFilter(request, response, filterChain);

		// Then
		Mockito.verify(responseWriter).writeResponse(problemDetail);
	}

	@Test
	@DisplayName("ExceptionHandlerFilter 는 RuntimeException 발생할 경우 INTERNAL_SERVER_ERROR 반환한다")
	void case2() throws ServletException, IOException {
		// Given
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String message = "RuntimeException";
		URI uri = URI.create("uri");

		Mockito.doThrow(new RuntimeException(message)).when(filterChain).doFilter(request, response);
		Mockito.when(problemDetailUtil.createType(status)).thenReturn(uri);

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
		problemDetail.setType(uri);

		// When
		jwtExceptionHandler.doFilter(request, response, filterChain);

		// Then
		Mockito.verify(responseWriter).writeResponse(Mockito.any(ProblemDetail.class));
	}
}
