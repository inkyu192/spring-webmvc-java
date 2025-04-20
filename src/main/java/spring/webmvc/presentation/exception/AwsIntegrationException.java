package spring.webmvc.presentation.exception;

import org.springframework.http.HttpStatus;

public class AwsIntegrationException extends AbstractHttpException {

	public AwsIntegrationException(String serviceName, Throwable throwable) {
		super(
			"%s 서비스와의 통신 중 오류가 발생했습니다.".formatted(serviceName),
			HttpStatus.INTERNAL_SERVER_ERROR,
			throwable
		);
	}
}
