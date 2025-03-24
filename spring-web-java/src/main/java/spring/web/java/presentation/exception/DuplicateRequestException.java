package spring.web.java.presentation.exception;

import org.springframework.http.HttpStatus;

public class DuplicateRequestException extends AbstractHttpException {

	public DuplicateRequestException(Long memberId, String method, String uri) {
		super("회원(ID: %d)의 요청이 중복되었습니다. (%s %s)".formatted(memberId, method, uri), HttpStatus.TOO_MANY_REQUESTS);
	}
}
