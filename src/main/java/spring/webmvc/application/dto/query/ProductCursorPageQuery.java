package spring.webmvc.application.dto.query;

import java.util.List;

import spring.webmvc.domain.model.enums.ProductStatus;

public record ProductCursorPageQuery(
	Long cursorId,
	String name,
	ProductStatus status,
	List<Long> tagIds
) {
	public ProductCursorPageQuery(Long cursorId, String name, ProductStatus status) {
		this(cursorId, name, status, List.of());
	}
}
