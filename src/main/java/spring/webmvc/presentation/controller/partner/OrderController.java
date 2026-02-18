package spring.webmvc.presentation.controller.partner;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.aspect.RequestLock;
import spring.webmvc.application.dto.command.OrderStatusUpdateCommand;
import spring.webmvc.application.dto.query.OrderOffsetPageQuery;
import spring.webmvc.application.dto.result.OrderDetailResult;
import spring.webmvc.application.dto.result.OrderSummaryResult;
import spring.webmvc.application.service.OrderService;
import spring.webmvc.domain.model.enums.OrderStatus;
import spring.webmvc.presentation.dto.request.OrderStatusUpdateRequest;
import spring.webmvc.presentation.dto.response.OffsetPageResponse;
import spring.webmvc.presentation.dto.response.OrderDetailResponse;
import spring.webmvc.presentation.dto.response.OrderSummaryResponse;

@RestController("partnerOrderController")
@RequestMapping("/partner/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@GetMapping
	@PreAuthorize("hasAuthority('ORDER_READ')")
	public OffsetPageResponse<OrderSummaryResponse> findOrders(
		@PageableDefault Pageable pageable,
		@RequestParam(required = false) Long userId,
		@RequestParam(required = false) OrderStatus status,
		@RequestParam(required = false) Instant orderedFrom,
		@RequestParam(required = false) Instant orderedTo
	) {
		OrderOffsetPageQuery query = new OrderOffsetPageQuery(pageable, userId, status, orderedFrom, orderedTo);

		Page<OrderSummaryResult> page = orderService.findOrdersWithOffsetPage(query);

		return OffsetPageResponse.of(page, OrderSummaryResponse::of);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ORDER_READ')")
	public OrderDetailResponse findOrder(@PathVariable Long id) {
		OrderDetailResult result = orderService.findOrder(id);

		return OrderDetailResponse.of(result);
	}

	@PatchMapping("/{id}")
	@PreAuthorize("hasAuthority('ORDER_WRITE')")
	@RequestLock
	public OrderDetailResponse updateOrderStatus(
		@PathVariable Long id,
		@RequestBody @Validated OrderStatusUpdateRequest request
	) {
		OrderStatusUpdateCommand command = request.toCommand(id);

		OrderDetailResult result = orderService.updateOrderStatus(command);

		return OrderDetailResponse.of(result);
	}
}
