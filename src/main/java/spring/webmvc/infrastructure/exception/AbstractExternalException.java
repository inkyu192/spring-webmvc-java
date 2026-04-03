package spring.webmvc.infrastructure.exception;

import lombok.Getter;

@Getter
public class AbstractExternalException extends RuntimeException {

	private final Object[] messageArgs;

	public AbstractExternalException(Throwable throwable, Object... messageArgs) {
		super(throwable);
		this.messageArgs = messageArgs;
	}

	public String getTranslationCode() {
		return this.getClass().getSimpleName();
	}
}
