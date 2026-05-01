package spring.webmvc.presentation.dto.response;

import spring.webmvc.application.dto.result.TagResult;

public record TagResponse(Long id, String name) {
	public static TagResponse of(TagResult result) {
		return new TagResponse(result.id(), result.name());
	}
}
