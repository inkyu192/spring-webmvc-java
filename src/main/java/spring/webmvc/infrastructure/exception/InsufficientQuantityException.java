package spring.webmvc.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class InsufficientQuantityException extends AbstractHttpException {

	public InsufficientQuantityException(String productName, Long requestQuantity, Long stock) {
		super(HttpStatus.CONFLICT, productName, requestQuantity, stock);
	}
}
