package spring.webmvc.presentation.controller.customer;

import java.time.Instant;

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
import spring.webmvc.application.aspect.RequestLock;
import spring.webmvc.application.dto.command.OrderCreateCommand;
import spring.webmvc.application.dto.query.OrderCursorPageQuery;
import spring.webmvc.application.dto.result.OrderDetailResult;
import spring.webmvc.application.dto.result.OrderSummaryResult;
import spring.webmvc.application.service.OrderService;
import spring.webmvc.domain.model.enums.OrderStatus;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;
import spring.webmvc.infrastructure.security.SecurityContextUtil;
import spring.webmvc.presentation.dto.request.OrderCreateRequest;
import spring.webmvc.presentation.dto.response.CursorPageResponse;
import spring.webmvc.presentation.dto.response.OrderDetailResponse;
import spring.webmvc.presentation.dto.response.OrderSummaryResponse;

@RestController("customerOrderController")
@RequestMapping("/customer/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	@RequestLock
	@ResponseStatus(HttpStatus.CREATED)
	public OrderDetailResponse createOrder(
		@RequestBody @Validated OrderCreateRequest request
	) {
		OrderCreateCommand command = request.toCommand(SecurityContextUtil.getUserId());

		OrderDetailResult result = orderService.createOrder(command);

		return OrderDetailResponse.of(result);
	}

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public CursorPageResponse<OrderSummaryResponse> findOrders(
		@RequestParam(required = false) Long cursorId,
		@RequestParam(required = false) OrderStatus orderStatus,
		@RequestParam(required = false) Instant orderedFrom,
		@RequestParam(required = false) Instant orderedTo
	) {
		OrderCursorPageQuery query = new OrderCursorPageQuery(
			cursorId,
			SecurityContextUtil.getUserId(),
			orderStatus,
			orderedFrom,
			orderedTo
		);

		CursorPage<OrderSummaryResult> page = orderService.findOrdersWithCursorPage(query);

		return CursorPageResponse.of(page, OrderSummaryResponse::of);
	}

	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public OrderDetailResponse findOrder(@PathVariable Long id) {
		OrderDetailResult result = orderService.findOrderByUser(id, SecurityContextUtil.getUserId());

		return OrderDetailResponse.of(result);
	}

	@PatchMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	@RequestLock
	public OrderDetailResponse cancelOrder(@PathVariable Long id) {
		OrderDetailResult result = orderService.cancelOrder(id);

		return OrderDetailResponse.of(result);
	}
}
