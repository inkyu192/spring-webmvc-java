package spring.webmvc.presentation.exception.handler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import spring.webmvc.infrastructure.properties.AppProperties;

@Component
@RequiredArgsConstructor
public class JwtExceptionHandler extends OncePerRequestFilter {
	private final AppProperties appProperties;
	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			HttpStatus status = (e instanceof JwtException)
				? HttpStatus.UNAUTHORIZED
				: HttpStatus.INTERNAL_SERVER_ERROR;

			ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, e.getMessage());
			problemDetail.setType(URI.create("%s#%s".formatted(appProperties.docsUrl(), status.value())));

			response.setStatus(status.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
		}
	}
}
