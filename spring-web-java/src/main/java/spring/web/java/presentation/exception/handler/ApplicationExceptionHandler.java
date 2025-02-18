package spring.web.java.presentation.exception.handler;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import spring.web.java.presentation.exception.BaseException;

@RestControllerAdvice
public class ApplicationExceptionHandler {

	@ExceptionHandler(BaseException.class)
	public ProblemDetail handleBaseException(BaseException exception) {
		return exception.getBody();
	}

	@ExceptionHandler({NoResourceFoundException.class, HttpRequestMethodNotSupportedException.class})
	public ProblemDetail handleResourceNotFound(ErrorResponse errorResponse) {
		return errorResponse.getBody();
	}

	@ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
	public ProblemDetail handleInvalidRequestBody(Exception exception) {
		ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problemDetail.setDetail(exception.getMessage());

		return problemDetail;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidationException(MethodArgumentNotValidException exception) {
		ProblemDetail problemDetail = exception.getBody();
		problemDetail.setProperties(Map.of("fieldErrors", exception.getBindingResult().getFieldErrors().stream()
			.collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage))));

		return problemDetail;
	}

	@ExceptionHandler(ServletRequestBindingException.class)
	public ProblemDetail handleBindingException(ServletRequestBindingException exception) {
		return exception.getBody();
	}
}
