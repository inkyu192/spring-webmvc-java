package spring.webmvc.presentation.dto.request;

import java.util.List;

import spring.webmvc.domain.model.vo.CurationAttribute;

public record CurationAttributeRequest(List<Long> tagIds) {
	public CurationAttributeRequest {
		if (tagIds == null) {
			tagIds = List.of();
		}
	}

	public CurationAttribute toVo() {
		return new CurationAttribute(tagIds);
	}
}
