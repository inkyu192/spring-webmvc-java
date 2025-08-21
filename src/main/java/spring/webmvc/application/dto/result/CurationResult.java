package spring.webmvc.application.dto.result;

import spring.webmvc.domain.model.cache.CurationCache;
import spring.webmvc.domain.model.entity.Curation;

public record CurationResult(
	Long id,
	String title
) {
	public CurationResult(Curation curation) {
		this(
			curation.getId(),
			curation.getTitle()
		);
	}

	public CurationResult(CurationCache curation) {
		this(
			curation.id(),
			curation.title()
		);
	}
}