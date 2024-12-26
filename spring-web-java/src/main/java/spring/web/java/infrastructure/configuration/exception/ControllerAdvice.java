package spring.web.java.infrastructure.configuration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.web.java.infrastructure.configuration.filter.HttpLogFilter;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

	private final ServletRequest servletRequest;

	@ExceptionHandler
	public ProblemDetail handler(HttpRequestMethodNotSupportedException e) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.METHOD_NOT_ALLOWED);

		problemDetail.setTitle(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
		problemDetail.setDetail(e.getMessage());

		return problemDetail;
	}

	@ExceptionHandler
	public ProblemDetail handler(MethodArgumentNotValidException e) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

		problemDetail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
		problemDetail.setDetail(e.getMessage() + ": " + e.getBindingResult().getFieldErrors().stream()
			.map(FieldError::getField)
			.toList());

		return problemDetail;
	}

	@ExceptionHandler
	public ProblemDetail handler(JwtException e) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);

		problemDetail.setTitle(HttpStatus.UNAUTHORIZED.getReasonPhrase());
		problemDetail.setDetail(e.getMessage());

		return problemDetail;
	}

	@ExceptionHandler
	public ProblemDetail handler(Exception e) {
		log.error("[{}]", servletRequest.getAttribute(HttpLogFilter.TRANSACTION_ID), e);

		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

		problemDetail.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		problemDetail.setDetail(e.getMessage());

		return problemDetail;
	}
}
