package spring.webmvc.presentation.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public abstract class AbstractValidationException extends AbstractHttpException {

	public AbstractValidationException(String message) {
		super(message, HttpStatus.BAD_REQUEST);
	}
}
