package spring.web.java.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

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
}
