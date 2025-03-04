package spring.web.java.presentation.exception;

import org.springframework.http.HttpStatus;

public class OrderCancelNotAllowedException extends BusinessException {

	public OrderCancelNotAllowedException(Long orderId) {
		super("주문(ID: %d)은 현재 상태에서는 취소할 수 없습니다.".formatted(orderId), HttpStatus.UNPROCESSABLE_ENTITY);
	}
}
