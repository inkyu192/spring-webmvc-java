package spring.webmvc.application.dto.query;

import java.time.Instant;

import org.springframework.data.domain.Pageable;

import spring.webmvc.domain.model.enums.OrderStatus;

public record OrderOffsetPageQuery(
	Pageable pageable,
	Long userId,
	OrderStatus orderStatus,
	Instant orderedFrom,
	Instant orderedTo
) {
}
