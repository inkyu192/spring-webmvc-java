package spring.webmvc.infrastructure.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public abstract class AbstractHttpException extends RuntimeException {

	private final HttpStatus httpStatus;
	private final Object[] messageArgs;

	protected AbstractHttpException(HttpStatus httpStatus, Object... messageArgs) {
		this.httpStatus = httpStatus;
		this.messageArgs = messageArgs;
	}

	public String getTranslationCode() {
		return this.getClass().getSimpleName();
	}
}
