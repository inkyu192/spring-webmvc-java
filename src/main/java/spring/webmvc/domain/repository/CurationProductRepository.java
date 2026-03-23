package spring.webmvc.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.entity.CurationProduct;

public interface CurationProductRepository {
	CursorPage<CurationProduct> findAllWithCursorPage(Long curationId, Long cursorId);

	Page<CurationProduct> findAllWithOffsetPage(Long curationId, Pageable pageable);
}
