package spring.webmvc.presentation.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends AbstractHttpException {

	public EntityNotFoundException(Class<?> clazz, Long id) {
		super("%s 엔티티를 찾을 수 없습니다. (ID: %s)".formatted(clazz.getSimpleName(), id), HttpStatus.NOT_FOUND);
	}
}
