package spring.webmvc.domain.model.cache;

import spring.webmvc.infrastructure.persistence.dto.CursorPage;

public record CurationProductCache(
	CurationCache curation,
	CursorPage<ProductCache> productPage
) {
}
