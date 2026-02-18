package spring.webmvc.presentation.exception.handler;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.RequiredArgsConstructor;
import spring.webmvc.infrastructure.exception.AbstractHttpException;
import spring.webmvc.infrastructure.properties.AppProperties;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler {

	private final AppProperties appProperties;

	@ExceptionHandler(AbstractHttpException.class)
	public ProblemDetail handleBusinessException(AbstractHttpException e) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(e.getHttpStatus(), e.getMessage());
		problem.setType(URI.create("%s#%s".formatted(appProperties.docsUrl(), problem.getStatus())));
		return problem;
	}

	@ExceptionHandler({
		NoResourceFoundException.class,
		HttpRequestMethodNotSupportedException.class,
		ServletRequestBindingException.class
	})
	public ProblemDetail handleResourceNotFound(ErrorResponse errorResponse) {
		ProblemDetail body = errorResponse.getBody();
		body.setType(URI.create("%s#%s".formatted(appProperties.docsUrl(), body.getStatus())));
		return body;
	}

	@ExceptionHandler({
		HttpMessageNotReadableException.class,
		MethodArgumentTypeMismatchException.class
	})
	public ProblemDetail handleInvalidRequestBody(Exception exception) {
		HttpStatus status = HttpStatus.BAD_REQUEST;

		ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
		problem.setType(URI.create("%s#%s".formatted(appProperties.docsUrl(), problem.getStatus())));
		return problem;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidationException(MethodArgumentNotValidException exception) {
		ProblemDetail body = exception.getBody();
		body.setType(URI.create("%s#%s".formatted(appProperties.docsUrl(), body.getStatus())));

		Map<String, String> fields = exception.getBindingResult().getFieldErrors().stream()
			.collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));

		body.setProperty("fields", fields);
		return body;
	}
}
