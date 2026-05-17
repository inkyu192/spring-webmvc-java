package spring.webmvc.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class OrderNumberGenerationException extends AbstractHttpException {
	public OrderNumberGenerationException() {
		super(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
