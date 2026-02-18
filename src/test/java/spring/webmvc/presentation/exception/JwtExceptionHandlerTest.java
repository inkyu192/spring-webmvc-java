package spring.webmvc.presentation.exception;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import spring.webmvc.infrastructure.properties.AppProperties;
import spring.webmvc.presentation.exception.handler.JwtExceptionHandler;

@ExtendWith(MockitoExtension.class)
class JwtExceptionHandlerTest {

	@Mock
	private FilterChain filterChain;

	@Mock
	private AppProperties appProperties;

	@Mock
	private ObjectMapper objectMapper;

	private JwtExceptionHandler jwtExceptionHandler;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@BeforeEach
	void setUp() {
		jwtExceptionHandler = new JwtExceptionHandler(appProperties, objectMapper);
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	@Test
	@DisplayName("JwtException 발생할 경우 ProblemDetail 반환한다")
	void doFilterWhenJwtExceptionOccurs() throws Exception {
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		String message = "JwtException";
		String docsUrl = "http://localhost:8080/docs/index.html";

		String problemDetailJson = "{\"type\":\"%s#%s\",\"title\":\"Unauthorized\",\"status\":401,\"detail\":\"%s\"}"
			.formatted(docsUrl, status.name(), message);

		doThrow(new JwtException(message)).when(filterChain).doFilter(request, response);
		when(appProperties.docsUrl()).thenReturn(docsUrl);
		when(objectMapper.writeValueAsString(any(ProblemDetail.class))).thenReturn(problemDetailJson);

		jwtExceptionHandler.doFilter(request, response, filterChain);

		verify(objectMapper).writeValueAsString(any(ProblemDetail.class));
	}

	@Test
	@DisplayName("RuntimeException 발생할 경우 ProblemDetail 반환한다")
	void doFilterWhenRuntimeExceptionOccurs() throws Exception {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String message = "RuntimeException";
		String docsUrl = "http://localhost:8080/docs/index.html";

		String problemDetailJson = "{\"type\":\"%s#%s\",\"title\":\"Internal Server Error\",\"status\":500,\"detail\":\"%s\"}"
			.formatted(docsUrl, status.name(), message);

		doThrow(new RuntimeException(message)).when(filterChain).doFilter(request, response);
		when(appProperties.docsUrl()).thenReturn(docsUrl);
		when(objectMapper.writeValueAsString(any(ProblemDetail.class))).thenReturn(problemDetailJson);

		jwtExceptionHandler.doFilter(request, response, filterChain);

		verify(objectMapper).writeValueAsString(any(ProblemDetail.class));
	}
}
