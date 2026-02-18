package spring.webmvc.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class NotFoundEntityException extends AbstractHttpException {

	public NotFoundEntityException(Class<?> clazz, Object id) {
		super(
			HttpStatus.NOT_FOUND,
			"%s 엔티티를 찾을 수 없습니다. (ID: %s)".formatted(clazz.getSimpleName(), id)
		);
	}
}
