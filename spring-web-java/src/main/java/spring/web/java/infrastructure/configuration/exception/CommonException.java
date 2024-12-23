package spring.web.java.infrastructure.configuration.exception;

import lombok.Getter;
import spring.web.java.common.ApiResponseCode;

@Getter
public class CommonException extends RuntimeException {

	private final ApiResponseCode apiResponseCode;

	public CommonException(ApiResponseCode apiResponseCode) {
		this.apiResponseCode = apiResponseCode;
	}
}
