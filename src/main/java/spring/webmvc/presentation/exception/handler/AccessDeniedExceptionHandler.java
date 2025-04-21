package spring.webmvc.presentation.exception.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import spring.webmvc.infrastructure.common.UriFactory;
import spring.webmvc.infrastructure.common.ResponseWriter;

@Component
@RequiredArgsConstructor
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

	private final UriFactory uriFactory;
	private final ResponseWriter responseWriter;

	@Override
	public void handle(
		HttpServletRequest request,
		HttpServletResponse response,
		AccessDeniedException exception
	) throws IOException {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, exception.getMessage());
		problemDetail.setType(uriFactory.createApiDocUri(HttpStatus.FORBIDDEN));

		responseWriter.writeResponse(problemDetail);
	}
}
