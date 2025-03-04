package spring.web.java.presentation.exception.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import spring.web.java.infrastructure.util.ResponseWriter;

@Component
@RequiredArgsConstructor
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

	private final ResponseWriter responseWriter;

	@Override
	public void handle(
		HttpServletRequest request,
		HttpServletResponse response,
		AccessDeniedException accessDeniedException
	) throws IOException {
		responseWriter.writeResponse(
			ProblemDetail.forStatusAndDetail(
				HttpStatus.FORBIDDEN,
				accessDeniedException.getMessage()
			)
		);
	}
}
