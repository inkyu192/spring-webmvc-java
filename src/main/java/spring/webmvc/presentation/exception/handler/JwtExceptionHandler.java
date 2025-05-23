package spring.webmvc.presentation.exception.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.infrastructure.common.ResponseWriter;
import spring.webmvc.infrastructure.common.UriFactory;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtExceptionHandler extends OncePerRequestFilter {

	private final UriFactory uriFactory;
	private final ResponseWriter responseWriter;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (JwtException e) {
			handleException(HttpStatus.UNAUTHORIZED, e.getMessage());
		} catch (Exception e) {
			log.error("Unexpected error occurred: {}", e.getMessage(), e);
			handleException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private void handleException(HttpStatus status, String message) throws IOException {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
		problemDetail.setType(uriFactory.createApiDocUri(status));

		responseWriter.writeResponse(problemDetail);
	}
}
