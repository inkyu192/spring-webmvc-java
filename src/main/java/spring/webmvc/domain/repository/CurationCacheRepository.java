package spring.webmvc.domain.repository;

import java.util.List;

import spring.webmvc.domain.model.cache.CurationCache;
import spring.webmvc.domain.model.cache.CurationProductCache;

public interface CurationCacheRepository {
	void setCurations(List<CurationCache> curations);

	List<CurationCache> getCurations();

	void setCurationProducts(Long curationId, Long cursorId, Integer size, CurationProductCache cache);

	CurationProductCache getCurationProducts(Long curationId, Long cursorId, Integer size);

	void deleteAll();
}
