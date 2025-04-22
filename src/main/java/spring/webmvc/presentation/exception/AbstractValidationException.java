package spring.webmvc.presentation.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public abstract class AbstractValidationException extends AbstractHttpException {

	private final List<String> fields;

	public AbstractValidationException(String message, String... fields) {
		super(message, HttpStatus.BAD_REQUEST);
		this.fields = List.of(fields);
	}
}
