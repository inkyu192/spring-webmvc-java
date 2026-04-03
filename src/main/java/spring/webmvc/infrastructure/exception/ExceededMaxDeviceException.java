package spring.webmvc.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class ExceededMaxDeviceException extends AbstractHttpException {

	public ExceededMaxDeviceException(int maxDevices) {
		super(HttpStatus.CONFLICT, maxDevices);
	}
}
