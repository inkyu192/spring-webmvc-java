package spring.webmvc.infrastructure.persistence.adapter;

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
	public CursorPage<CurationProduct> findAll(Long curationId, Long cursorId, Integer size) {
		return querydslRepository.findAll(curationId, cursorId, size);
	}
}
