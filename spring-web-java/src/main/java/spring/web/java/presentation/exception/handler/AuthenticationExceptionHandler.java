package spring.web.java.presentation.exception.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import spring.web.java.infrastructure.util.ResponseWriter;

@Component
@RequiredArgsConstructor
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {

	private final ResponseWriter responseWriter;

	@Override
	public void commence(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException
	) throws IOException {
		responseWriter.writeResponse(
			ProblemDetail.forStatusAndDetail(
				HttpStatus.UNAUTHORIZED,
				authException.getMessage()
			)
		);
	}
}
