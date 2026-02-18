package spring.webmvc.infrastructure.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public abstract class AbstractHttpException extends RuntimeException {

	private final HttpStatus httpStatus;

	protected AbstractHttpException(HttpStatus httpStatus, String message) {
		super(message);
		this.httpStatus = httpStatus;
	}

	protected AbstractHttpException(HttpStatus httpStatus, String message, Throwable throwable) {
		super(message, throwable);
		this.httpStatus = httpStatus;
	}
}
