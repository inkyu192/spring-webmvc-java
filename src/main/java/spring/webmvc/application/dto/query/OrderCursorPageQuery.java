package spring.webmvc.application.dto.query;

import java.time.Instant;

import spring.webmvc.domain.model.enums.OrderStatus;

public record OrderCursorPageQuery(
	Long cursorId,
	Long userId,
	OrderStatus orderStatus,
	Instant orderedFrom,
	Instant orderedTo
) {
}
