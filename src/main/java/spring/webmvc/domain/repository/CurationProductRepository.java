package spring.webmvc.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.webmvc.domain.model.entity.CurationProduct;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

public interface CurationProductRepository {
	CursorPage<CurationProduct> findAllWithCursorPage(Long curationId, Long cursorId);

	Page<CurationProduct> findAllWithOffsetPage(Long curationId, Pageable pageable);
}
