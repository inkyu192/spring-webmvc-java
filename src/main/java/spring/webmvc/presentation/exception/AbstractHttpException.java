package spring.webmvc.presentation.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public abstract class AbstractHttpException extends RuntimeException {

	private final HttpStatus httpStatus;

	public AbstractHttpException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public AbstractHttpException(String message, HttpStatus httpStatus, Throwable throwable) {
		super(message, throwable);
		this.httpStatus = httpStatus;
	}
}
