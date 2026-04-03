package spring.webmvc.infrastructure.exception;

public class FailedAwsException extends AbstractExternalException {

	public FailedAwsException(String serviceName, Throwable throwable) {
		super(throwable, serviceName);
	}
}
