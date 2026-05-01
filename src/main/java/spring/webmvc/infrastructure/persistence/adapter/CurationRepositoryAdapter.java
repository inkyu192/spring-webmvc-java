package spring.webmvc.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.enums.CurationPlacement;
import spring.webmvc.domain.repository.CurationRepository;
import spring.webmvc.infrastructure.persistence.jpa.CurationJpaRepository;

@Component
@RequiredArgsConstructor
public class CurationRepositoryAdapter implements CurationRepository {

	private final CurationJpaRepository jpaRepository;

	@Override
	public Optional<Curation> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public List<Curation> findAllByPlacement(CurationPlacement placement) {
		return jpaRepository.findByPlacementAndIsExposedIsTrueOrderBySortOrder(placement);
	}

	@Override
	public Curation save(Curation curation) {
		return jpaRepository.save(curation);
	}
}
