package spring.webmvc.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class InvalidEntityStatusException extends AbstractHttpException {

	public InvalidEntityStatusException(Class<?> clazz, Long id, String fromStatus, String toStatus) {
		super(
			HttpStatus.UNPROCESSABLE_ENTITY,
			"%s(ID: %s)의 상태를 %s에서 %s(으)로 변경할 수 없습니다.".formatted(clazz.getSimpleName(), id, fromStatus, toStatus)
		);
	}
}
