package spring.webmvc.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class FailedAwsIntegrationException extends AbstractHttpException {

	public FailedAwsIntegrationException(String serviceName, Throwable throwable) {
		super(HttpStatus.BAD_GATEWAY, throwable, serviceName);
	}
}
