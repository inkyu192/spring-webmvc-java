package spring.webmvc.domain.repository;

import java.util.List;
import java.util.Optional;

import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.enums.CurationPlacement;

public interface CurationRepository {
	Optional<Curation> findById(Long id);

	List<Curation> findAllByPlacement(CurationPlacement placement);

	Curation save(Curation curation);
}
