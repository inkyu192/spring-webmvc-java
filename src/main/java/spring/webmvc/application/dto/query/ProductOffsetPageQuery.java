package spring.webmvc.application.dto.query;

import java.util.List;

import org.springframework.data.domain.Pageable;

import spring.webmvc.domain.model.enums.ProductStatus;

public record ProductOffsetPageQuery(
	Pageable pageable,
	String name,
	ProductStatus status,
	List<Long> tagIds
) {
	public ProductOffsetPageQuery(Pageable pageable, String name, ProductStatus status) {
		this(pageable, name, status, List.of());
	}
}
