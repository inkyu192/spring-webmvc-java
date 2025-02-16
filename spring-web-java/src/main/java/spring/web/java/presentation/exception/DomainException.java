package spring.web.java.presentation.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class DomainException extends BaseException {

	private final ResponseMessage responseMessage;

	public DomainException(ResponseMessage responseMessage, HttpStatus httpStatus) {
		super(responseMessage.getDetail(), httpStatus);
		this.responseMessage = responseMessage;
	}
}
