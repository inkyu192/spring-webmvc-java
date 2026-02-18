package spring.webmvc.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class FailedAwsIntegrationException extends AbstractHttpException {

	public FailedAwsIntegrationException(String serviceName, Throwable throwable) {
		super(
			HttpStatus.BAD_GATEWAY,
			"%s 서비스와의 통신 중 오류가 발생했습니다.".formatted(serviceName),
			throwable
		);
	}
}
