package spring.web.java.infrastructure.adapter.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import spring.web.java.application.port.in.OrderServicePort;
import spring.web.java.common.constant.DeliveryStatus;
import spring.web.java.common.constant.OrderStatus;
import spring.web.java.dto.request.OrderSaveRequest;
import spring.web.java.dto.response.OrderResponse;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

	private final OrderServicePort orderService;

	@PostMapping
	public OrderResponse saveOrder(@RequestBody @Valid OrderSaveRequest orderSaveRequest) {
		return orderService.saveOrder(orderSaveRequest);
	}

	@GetMapping
	public Page<OrderResponse> findOrders(
		Pageable pageable,
		@RequestParam Long memberId,
		@RequestParam(required = false) OrderStatus orderStatus,
		@RequestParam(required = false) DeliveryStatus deliveryStatus
	) {
		return orderService.findOrders(memberId, orderStatus, deliveryStatus, pageable);
	}

	@GetMapping("{id}")
	public OrderResponse findOrder(@PathVariable Long id) {
		return orderService.findOrder(id);
	}

	@PostMapping("{id}")
	public OrderResponse cancelOrder(@PathVariable Long id) {
		return orderService.cancelOrder(id);
	}
}
