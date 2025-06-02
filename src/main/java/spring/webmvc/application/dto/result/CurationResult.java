package spring.webmvc.application.dto.result;

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
}