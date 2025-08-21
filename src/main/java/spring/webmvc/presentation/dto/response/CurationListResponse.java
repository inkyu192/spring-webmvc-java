package spring.webmvc.presentation.dto.response;

import java.util.List;

import spring.webmvc.application.dto.result.CurationResult;

public record CurationListResponse(
	long count,
	List<CurationResponse> curations
) {
	public CurationListResponse(List<CurationResult> resultList) {
		this(
			resultList.size(),
			resultList.stream().map(CurationResponse::new).toList()
		);
	}
}
