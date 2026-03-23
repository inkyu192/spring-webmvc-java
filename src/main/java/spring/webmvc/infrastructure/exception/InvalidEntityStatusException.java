package spring.webmvc.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class InvalidEntityStatusException extends AbstractHttpException {

	public InvalidEntityStatusException(Class<?> clazz, Long id, String fromStatus, String toStatus) {
		super(HttpStatus.UNPROCESSABLE_ENTITY, clazz.getSimpleName(), id, fromStatus, toStatus);
	}
}
