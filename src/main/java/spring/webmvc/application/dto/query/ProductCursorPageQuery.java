package spring.webmvc.application.dto.query;

import spring.webmvc.domain.model.enums.ProductStatus;

public record ProductCursorPageQuery(
	Long cursorId,
	String name,
	ProductStatus status
) {
}
