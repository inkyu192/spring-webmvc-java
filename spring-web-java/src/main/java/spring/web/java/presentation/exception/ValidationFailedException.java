package spring.web.java.presentation.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public abstract class ValidationFailedException extends BusinessException {

	public ValidationFailedException(String message) {
		super(message, HttpStatus.BAD_REQUEST);
	}
}
