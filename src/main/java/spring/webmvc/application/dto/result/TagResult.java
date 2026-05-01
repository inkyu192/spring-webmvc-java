package spring.webmvc.application.dto.result;

import spring.webmvc.domain.model.entity.Tag;

public record TagResult(Long id, String name) {
	public static TagResult of(Tag tag) {
		return new TagResult(tag.getId(), tag.getName());
	}
}
