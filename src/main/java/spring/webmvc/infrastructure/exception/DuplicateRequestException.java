package spring.webmvc.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class DuplicateRequestException extends AbstractHttpException {

	public DuplicateRequestException(String method, String uri) {
		super(
			HttpStatus.TOO_MANY_REQUESTS,
			"요청이 중복되었습니다. (%s %s)".formatted(method, uri)
		);
	}
}
