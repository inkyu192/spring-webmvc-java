package spring.web.java.presentation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BaseException extends RuntimeException implements org.springframework.web.ErrorResponse {

	private final ErrorResponse errorResponse;
	private final HttpStatus httpStatus;

	@Override
	public HttpStatusCode getStatusCode() {
		return httpStatus;
	}

	@Override
	public ProblemDetail getBody() {
		ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus);
		problemDetail.setTitle(httpStatus.getReasonPhrase());
		problemDetail.setDetail(errorResponse.getDetail());

		return problemDetail;
	}
}
