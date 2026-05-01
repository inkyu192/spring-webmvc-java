package spring.webmvc.domain.model.vo;

import java.util.List;

public record CurationAttribute(List<Long> tagIds) {
	public CurationAttribute {
		if (tagIds == null) {
			tagIds = List.of();
		}
	}

	public CurationAttribute() {
		this(List.of());
	}
}
