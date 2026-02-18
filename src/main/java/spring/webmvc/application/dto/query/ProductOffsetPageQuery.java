package spring.webmvc.application.dto.query;

import org.springframework.data.domain.Pageable;

import spring.webmvc.domain.model.enums.ProductStatus;

public record ProductOffsetPageQuery(
	Pageable pageable,
	String name,
	ProductStatus status
) {
}
