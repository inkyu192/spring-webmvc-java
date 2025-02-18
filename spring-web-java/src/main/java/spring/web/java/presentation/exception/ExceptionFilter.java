package spring.web.java.presentation.exception;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.web.java.infrastructure.logging.HttpLogFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (JwtException | AuthenticationException e) {
			handleException(response, HttpStatus.UNAUTHORIZED, e.getMessage());
		} catch (AccessDeniedException e) {
			handleException(response, HttpStatus.FORBIDDEN, e.getMessage());
		} catch (Exception e) {
			log.error("[{}]", request.getAttribute(HttpLogFilter.TRANSACTION_ID), e);
			handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private void handleException(HttpServletResponse response, HttpStatus status, String message) throws IOException {
		ProblemDetail problemDetail = generateProblemDetail(status, message);
		writeResponse(response, problemDetail);
	}

	private ProblemDetail generateProblemDetail(HttpStatus status, String detail) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(status);
		problemDetail.setTitle(status.getReasonPhrase());
		problemDetail.setDetail(detail);
		return problemDetail;
	}

	private void writeResponse(HttpServletResponse response, ProblemDetail problemDetail) throws IOException {
		response.setStatus(problemDetail.getStatus());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
		response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
	}
}
