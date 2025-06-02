package spring.webmvc.domain.repository;

import java.util.List;

import spring.webmvc.domain.model.entity.Curation;

public interface CurationRepository {
	List<Curation> findExposed();

	Curation save(Curation curation);
}
