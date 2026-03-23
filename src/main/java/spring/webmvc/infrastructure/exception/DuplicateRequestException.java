package spring.webmvc.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class DuplicateRequestException extends AbstractHttpException {

	public DuplicateRequestException(String method, String uri) {
		super(HttpStatus.TOO_MANY_REQUESTS, method, uri);
	}
}
