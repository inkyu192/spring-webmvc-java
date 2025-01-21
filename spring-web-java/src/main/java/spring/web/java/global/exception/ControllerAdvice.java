package spring.web.java.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.web.java.global.filter.HttpLogFilter;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

	private final HttpServletRequest httpServletRequest;

	@ExceptionHandler(DomainException.class)
	public ProblemDetail domain(DomainException exception) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(exception.getHttpStatus());
		problemDetail.setTitle(exception.getResponseMessage().getTitle());
		problemDetail.setDetail(exception.getResponseMessage().getDetail());

		return problemDetail;
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ProblemDetail methodNotAllowed(HttpRequestMethodNotSupportedException exception) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.METHOD_NOT_ALLOWED);
		problemDetail.setTitle(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
		problemDetail.setDetail(exception.getMessage());

		return problemDetail;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail badRequest(MethodArgumentNotValidException exception) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problemDetail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
		problemDetail.setDetail(
			"%s: %s".formatted(
				exception.getMessage(),
				exception.getBindingResult().getFieldErrors().stream()
					.map(FieldError::getField)
					.toList()
			)
		);

		return problemDetail;
	}

	@ExceptionHandler({AuthenticationException.class, AuthorizationDeniedException.class, JwtException.class})
	public ProblemDetail unauthorized(Exception exception) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
		problemDetail.setTitle(HttpStatus.UNAUTHORIZED.getReasonPhrase());
		problemDetail.setDetail(exception.getMessage());

		return problemDetail;
	}

	@ExceptionHandler(Exception.class)
	public ProblemDetail internalServerError(Exception exception) {
		log.error("[{}]", httpServletRequest.getAttribute(HttpLogFilter.TRANSACTION_ID), exception);

		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		problemDetail.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		problemDetail.setDetail(exception.getMessage());

		return problemDetail;
	}
}
