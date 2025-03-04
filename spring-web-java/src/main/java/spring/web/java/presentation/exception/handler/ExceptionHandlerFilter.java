package spring.web.java.presentation.exception.handler;

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
import spring.web.java.infrastructure.logging.HttpLogFilter;
import spring.web.java.infrastructure.util.ResponseWriter;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

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
			log.error("[{}]", request.getAttribute(HttpLogFilter.TRANSACTION_ID), e);
			handleException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private void handleException(HttpStatus status, String message) throws IOException {
		responseWriter.writeResponse(ProblemDetail.forStatusAndDetail(status, message));
	}
}
