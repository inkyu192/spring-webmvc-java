package spring.webmvc.application.exception;

import org.springframework.http.HttpStatus;

import spring.webmvc.presentation.exception.AbstractHttpException;

public class DuplicateRequestException extends AbstractHttpException {

	public DuplicateRequestException(String method, String uri) {
		super("요청이 중복되었습니다. (%s %s)".formatted(method, uri), HttpStatus.TOO_MANY_REQUESTS);
	}
}
