package spring.webmvc.presentation.exception.handler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import spring.webmvc.infrastructure.properties.AppProperties;

@Component
@RequiredArgsConstructor
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {
	private final AppProperties appProperties;
	private final ObjectMapper objectMapper;

	@Override
	public void handle(
		HttpServletRequest request,
		HttpServletResponse response,
		AccessDeniedException exception
	) throws IOException {
		HttpStatus status = HttpStatus.FORBIDDEN;

		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
		problemDetail.setType(URI.create("%s#%s".formatted(appProperties.docsUrl(), status)));

		response.setStatus(status.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
	}
}
