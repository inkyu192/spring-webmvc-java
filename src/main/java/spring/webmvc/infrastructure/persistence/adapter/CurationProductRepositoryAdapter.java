package spring.webmvc.infrastructure.persistence.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.CurationProduct;
import spring.webmvc.domain.repository.CurationProductRepository;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;
import spring.webmvc.infrastructure.persistence.jpa.CurationProductQuerydslRepository;

@Component
@RequiredArgsConstructor
public class CurationProductRepositoryAdapter implements CurationProductRepository {

	private final CurationProductQuerydslRepository querydslRepository;

	@Override
	public CursorPage<CurationProduct> findAllWithCursorPage(Long curationId, Long cursorId) {
		return querydslRepository.findAllWithCursorPage(curationId, cursorId);
	}

	@Override
	public Page<CurationProduct> findAllWithOffsetPage(Long curationId, Pageable pageable) {
		return querydslRepository.findAllWithOffsetPage(curationId, pageable);
	}
}
