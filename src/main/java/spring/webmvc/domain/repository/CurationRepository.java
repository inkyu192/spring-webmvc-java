package spring.webmvc.domain.repository;

import java.util.List;
import java.util.Optional;

import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.enums.CurationCategory;

public interface CurationRepository {
	Optional<Curation> findById(Long id);

	List<Curation> findAllByCategory(CurationCategory category);

	Curation save(Curation curation);
}
