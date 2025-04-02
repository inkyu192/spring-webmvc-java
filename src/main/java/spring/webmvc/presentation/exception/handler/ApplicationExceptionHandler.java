package spring.webmvc.presentation.exception.handler;

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

import spring.webmvc.presentation.exception.AtLeastOneRequiredException;
import spring.webmvc.presentation.exception.AbstractHttpException;

@RestControllerAdvice
public class ApplicationExceptionHandler {

	@ExceptionHandler(AbstractHttpException.class)
	public ProblemDetail handleBusinessException(AbstractHttpException e) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(e.getHttpStatus(), e.getMessage());

		if (e instanceof AtLeastOneRequiredException atLeastOneRequiredException) {
			problemDetail.setProperty("fields", atLeastOneRequiredException.getFields());
		}

		return problemDetail;
	}

	@ExceptionHandler({NoResourceFoundException.class, HttpRequestMethodNotSupportedException.class})
	public ProblemDetail handleResourceNotFound(ErrorResponse errorResponse) {
		return errorResponse.getBody();
	}

	@ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
	public ProblemDetail handleInvalidRequestBody(Exception exception) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidationException(MethodArgumentNotValidException exception) {
		ProblemDetail problemDetail = exception.getBody();
		problemDetail.setProperties(Map.of("fields", exception.getBindingResult().getFieldErrors().stream()
			.collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage))));

		return problemDetail;
	}

	@ExceptionHandler(ServletRequestBindingException.class)
	public ProblemDetail handleBindingException(ServletRequestBindingException exception) {
		return exception.getBody();
	}
}
