package spring.web.java.presentation.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEntityException extends BusinessException {

	public DuplicateEntityException(Class<?> clazz, String name) {
		super("이미 존재하는 %s 엔티티입니다. (ID: '%s')".formatted(clazz.getSimpleName(), name), HttpStatus.CONFLICT);
	}
}
