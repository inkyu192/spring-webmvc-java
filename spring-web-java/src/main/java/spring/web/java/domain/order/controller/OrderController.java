package spring.web.java.domain.order.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import spring.web.java.domain.order.Delivery;
import spring.web.java.domain.order.Order;
import spring.web.java.domain.order.dto.OrderResponse;
import spring.web.java.domain.order.dto.OrderSaveRequest;
import spring.web.java.domain.order.serivce.OrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrderResponse saveOrder(@RequestBody @Valid OrderSaveRequest orderSaveRequest) {
		return orderService.saveOrder(orderSaveRequest);
	}

	@GetMapping
	public Page<OrderResponse> findOrders(
		Pageable pageable,
		@RequestParam Long memberId,
		@RequestParam(required = false) Order.Status orderStatus,
		@RequestParam(required = false) Delivery.Status deliveryStatus
	) {
		return orderService.findOrders(memberId, orderStatus, deliveryStatus, pageable);
	}

	@GetMapping("/{id}")
	public OrderResponse findOrder(@PathVariable Long id) {
		return orderService.findOrder(id);
	}

	@PostMapping("/{id}")
	public OrderResponse cancelOrder(@PathVariable Long id) {
		return orderService.cancelOrder(id);
	}
}
