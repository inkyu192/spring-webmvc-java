package spring.webmvc.infrastructure.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.repository.CurationRepository;
import spring.webmvc.infrastructure.persistence.CurationJpaRepository;

@Component
@RequiredArgsConstructor
public class CurationRepositoryAdapter implements CurationRepository {

	private final CurationJpaRepository jpaRepository;

	@Override
	public List<Curation> findExposed() {
		return jpaRepository.findExposed();
	}

	@Override
	public Curation save(Curation curation) {
		return jpaRepository.save(curation);
	}
}
