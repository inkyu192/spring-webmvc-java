package spring.webmvc.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEntityException extends AbstractHttpException {

	public DuplicateEntityException(Class<?> clazz, String name) {
		super(
			HttpStatus.CONFLICT,
			"이미 존재하는 %s 엔티티입니다. (ID: %s)".formatted(clazz.getSimpleName(), name)
		);
	}
}
