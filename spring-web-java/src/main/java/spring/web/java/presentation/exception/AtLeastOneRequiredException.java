package spring.web.java.presentation.exception;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public class AtLeastOneRequiredException extends ValidationFailedException {

	private final List<String> fields;

	public AtLeastOneRequiredException(String... fields) {
		super("적어도 하나는 필수 입력값입니다.");
		this.fields = Arrays.asList(fields);
	}
}
