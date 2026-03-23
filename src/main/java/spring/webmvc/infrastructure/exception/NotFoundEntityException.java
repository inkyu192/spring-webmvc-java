package spring.webmvc.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class NotFoundEntityException extends AbstractHttpException {

	public NotFoundEntityException(Class<?> clazz, Object id) {
		super(HttpStatus.NOT_FOUND, clazz.getSimpleName(), id.toString());
	}
}
