package spring.web.java.global.exception;

import lombok.Getter;
import spring.web.java.global.common.ResponseMessage;

@Getter
public class CommonException extends RuntimeException {

	private final ResponseMessage responseMessage;

	public CommonException(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
}
