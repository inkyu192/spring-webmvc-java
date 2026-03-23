package spring.webmvc.presentation.exception.handler;

import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

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
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.service.TranslationService;
import spring.webmvc.infrastructure.exception.AbstractHttpException;
import spring.webmvc.infrastructure.properties.AppProperties;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler {

	private final AppProperties appProperties;
	private final TranslationService translationService;

	@ExceptionHandler(AbstractHttpException.class)
	public ProblemDetail handleBusinessException(AbstractHttpException e, Locale locale) {
		String detail = translationService.getMessage(e.getTranslationCode(), locale, e.getTranslationArgs());
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(e.getHttpStatus(), detail);
		problem.setType(URI.create("%s#%s".formatted(appProperties.docsUrl(), HttpStatus.valueOf(problem.getStatus()).name())));
		return problem;
	}

	@ExceptionHandler({
		NoResourceFoundException.class,
		HttpRequestMethodNotSupportedException.class,
		ServletRequestBindingException.class
	})
	public ProblemDetail handleResourceNotFound(ErrorResponse errorResponse, Locale locale) {
		ProblemDetail body = errorResponse.getBody();
		String detail = translationService.getMessage(errorResponse.getClass().getSimpleName(), locale);
		body.setDetail(detail);
		body.setType(URI.create("%s#%s".formatted(appProperties.docsUrl(), HttpStatus.valueOf(body.getStatus()).name())));
		return body;
	}

	@ExceptionHandler({
		HttpMessageNotReadableException.class,
		MethodArgumentTypeMismatchException.class,
		MethodArgumentConversionNotSupportedException.class
	})
	public ProblemDetail handleInvalidRequestBody(Exception exception, Locale locale) {
		HttpStatus status = HttpStatus.BAD_REQUEST;

		String detail = translationService.getMessage(exception.getClass().getSimpleName(), locale);
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
		problem.setType(URI.create("%s#%s".formatted(appProperties.docsUrl(), HttpStatus.valueOf(problem.getStatus()).name())));
		return problem;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidationException(MethodArgumentNotValidException exception, Locale locale) {
		String className = MethodArgumentNotValidException.class.getSimpleName();
		ProblemDetail body = exception.getBody();
		body.setType(URI.create("%s#%s".formatted(appProperties.docsUrl(), HttpStatus.valueOf(body.getStatus()).name())));
		body.setDetail(translationService.getMessage(className, locale));

		Map<String, String> fields = new LinkedHashMap<>();
		for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
			String code = fieldError.getCode();
			String translationCode = className + "." + code;
			Object[] args = fieldError.getArguments();
			Object[] translationArgs = (args != null && args.length > 1)
				? Arrays.copyOfRange(args, 1, args.length)
				: new Object[0];
			String message = translationService.getMessageOrNull(translationCode, locale, translationArgs);
			if (message == null) {
				message = translationService.getMessage(className + ".default", locale);
			}
			fields.put(fieldError.getField(), message);
		}

		body.setProperty("fields", fields);
		return body;
	}
}
