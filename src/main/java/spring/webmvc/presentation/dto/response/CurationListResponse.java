package spring.webmvc.presentation.dto.response;

import java.util.List;

import spring.webmvc.application.dto.result.CurationSummaryResult;

public record CurationListResponse(
	Long size,
	List<CurationSummaryResponse> curations
) {
	public static CurationListResponse of(List<CurationSummaryResult> results) {
		return new CurationListResponse(
			(long)results.size(),
			results.stream().map(CurationSummaryResponse::of).toList()
		);
	}
}
