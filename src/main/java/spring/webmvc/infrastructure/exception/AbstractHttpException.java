package spring.webmvc.infrastructure.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public abstract class AbstractHttpException extends RuntimeException {

	private final HttpStatus httpStatus;
	private final Object[] translationArgs;

	protected AbstractHttpException(HttpStatus httpStatus, Object... translationArgs) {
		this.httpStatus = httpStatus;
		this.translationArgs = translationArgs;
	}

	protected AbstractHttpException(HttpStatus httpStatus, Throwable throwable, Object... translationArgs) {
		super(throwable);
		this.httpStatus = httpStatus;
		this.translationArgs = translationArgs;
	}

	public String getTranslationCode() {
		return this.getClass().getSimpleName();
	}
}
