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
import spring.webmvc.infrastructure.support.ProblemDetailSupport;
import spring.webmvc.infrastructure.support.ResponseWriter;
import spring.webmvc.presentation.exception.handler.JwtExceptionHandler;

@ExtendWith(MockitoExtension.class)
class JwtExceptionHandlerTest {

	@InjectMocks
	private JwtExceptionHandler jwtExceptionHandler;

	@Mock
	private FilterChain filterChain;

	@Mock
	private ProblemDetailSupport problemDetailSupport;

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
	@DisplayName("doFilter: JwtException 발생할 경우 ProblemDetail 반환한다")
	void doFilterCase1() throws ServletException, IOException {
		// Given
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		String message = "JwtException";
		URI uri = URI.create("uri");

		Mockito.doThrow(new JwtException(message)).when(filterChain).doFilter(request, response);
		Mockito.when(problemDetailSupport.createType(status)).thenReturn(uri);

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
		problemDetail.setType(uri);

		// When
		jwtExceptionHandler.doFilter(request, response, filterChain);

		// Then
		Mockito.verify(responseWriter).writeResponse(problemDetail);
	}

	@Test
	@DisplayName("doFilter: ServletException 발생할 경우 ProblemDetail 반환한다")
	void doFilterCase2() throws ServletException, IOException {
		// Given
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String message = "ServletException";
		URI uri = URI.create("uri");

		Mockito.doThrow(new ServletException(message)).when(filterChain).doFilter(request, response);
		Mockito.when(problemDetailSupport.createType(status)).thenReturn(uri);

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
		problemDetail.setType(uri);

		// When
		jwtExceptionHandler.doFilter(request, response, filterChain);

		// Then
		Mockito.verify(responseWriter).writeResponse(Mockito.any(ProblemDetail.class));
	}
}
