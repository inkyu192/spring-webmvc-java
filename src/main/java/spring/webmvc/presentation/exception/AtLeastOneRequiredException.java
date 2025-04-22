package spring.webmvc.presentation.exception;

import lombok.Getter;

@Getter
public class AtLeastOneRequiredException extends AbstractValidationException {

	public AtLeastOneRequiredException(String... fields) {
		super("적어도 하나는 필수 입력값입니다.", fields);
	}
}
