package spring.webmvc.presentation.exception;

import org.springframework.http.HttpStatus;

public class InsufficientQuantityException extends AbstractHttpException {

	public InsufficientQuantityException(String productName, int requestedQuantity, int availableStock) {
		super(
			"%s 상품의 재고가 부족합니다. (요청: %d, 재고: %d)".formatted(productName, requestedQuantity, availableStock),
			HttpStatus.CONFLICT
		);
	}
}
