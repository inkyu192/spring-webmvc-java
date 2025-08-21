package spring.webmvc.domain.repository;

import java.util.List;
import java.util.Optional;

import spring.webmvc.domain.model.entity.Curation;

public interface CurationRepository {
	Optional<Curation> findById(Long id);

	List<Curation> findExposed();

	Curation save(Curation curation);
}
