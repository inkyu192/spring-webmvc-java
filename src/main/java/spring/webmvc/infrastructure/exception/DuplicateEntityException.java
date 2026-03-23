package spring.webmvc.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEntityException extends AbstractHttpException {

	public DuplicateEntityException(Class<?> clazz, String name) {
		super(HttpStatus.CONFLICT, clazz.getSimpleName(), name);
	}
}
