package spring.webmvc.presentation.dto.response;

import spring.webmvc.application.dto.result.CurationResult;

public record CurationResponse(
	Long id,
	String title
) {
	public CurationResponse(CurationResult curationResult) {
		this(curationResult.id(), curationResult.title());
	}
}