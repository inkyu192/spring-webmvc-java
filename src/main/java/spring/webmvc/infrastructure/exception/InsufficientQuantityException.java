package spring.webmvc.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class InsufficientQuantityException extends AbstractHttpException {

	public InsufficientQuantityException(String productName, Long requestQuantity, Long stock) {
		super(
			HttpStatus.CONFLICT,
			"%s 상품의 재고가 부족합니다. (요청수량: %s, 재고: %s)".formatted(productName, requestQuantity, stock)
		);
	}
}
