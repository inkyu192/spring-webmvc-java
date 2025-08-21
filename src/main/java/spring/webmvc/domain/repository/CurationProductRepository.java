package spring.webmvc.domain.repository;

import spring.webmvc.domain.model.entity.CurationProduct;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

public interface CurationProductRepository {
	CursorPage<CurationProduct> findAll(Long curationId, Long cursorId, Integer size);
}
