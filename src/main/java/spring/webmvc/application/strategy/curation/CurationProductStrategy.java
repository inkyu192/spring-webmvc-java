package spring.webmvc.application.strategy.curation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.webmvc.application.dto.result.CurationProductResult;
import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.enums.CurationType;

public interface CurationProductStrategy {

	CurationType getType();

	CursorPage<CurationProductResult> findProductsWithCursorPage(Curation curation, Long cursorId, Long userId);

	Page<CurationProductResult> findProductsWithOffsetPage(Curation curation, Pageable pageable);
}
