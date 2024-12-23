package spring.web.java.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.web.java.common.constant.DeliveryStatus;
import spring.web.java.common.constant.OrderStatus;
import spring.web.java.dto.request.OrderSaveRequest;
import spring.web.java.dto.response.OrderResponse;

public interface OrderServicePort {

	OrderResponse saveOrder(OrderSaveRequest orderSaveRequest);

	Page<OrderResponse> findOrders(
		Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus, Pageable pageable
	);

	OrderResponse findOrder(Long id);

	OrderResponse cancelOrder(Long id);
}
