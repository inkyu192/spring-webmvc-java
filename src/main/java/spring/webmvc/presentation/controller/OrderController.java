package spring.webmvc.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.command.OrderCreateCommand;
import spring.webmvc.application.service.OrderService;
import spring.webmvc.domain.model.enums.OrderStatus;
import spring.webmvc.infrastructure.aspect.RequestLock;
import spring.webmvc.presentation.dto.request.OrderCreateRequest;
import spring.webmvc.presentation.dto.response.OrderResponse;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	@RequestLock
	@ResponseStatus(HttpStatus.CREATED)
	public OrderResponse createOrder(@RequestBody @Validated OrderCreateRequest orderCreateRequest) {
		OrderCreateCommand orderCreateCommand = orderCreateRequest.toCommand();
		return new OrderResponse(orderService.createOrder(orderCreateCommand));
	}

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public Page<OrderResponse> findOrders(
		@PageableDefault Pageable pageable,
		@RequestParam(required = false) OrderStatus orderStatus
	) {
		return orderService.findOrders(pageable, orderStatus).map(OrderResponse::new);
	}

	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public OrderResponse findOrder(@PathVariable Long id) {
		return new OrderResponse(orderService.findOrder(id));
	}

	@PatchMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	@RequestLock
	public OrderResponse cancelOrder(@PathVariable Long id) {
		return new OrderResponse(orderService.cancelOrder(id));
	}
}
