package spring.webmvc.presentation.dto.response;

import java.util.List;

import spring.webmvc.application.dto.result.CodeGroupResult;

public record CodeListResponse(
	long size,
	List<CodeGroupResponse> codeGroups
) {
	public static CodeListResponse of(List<CodeGroupResult> results) {
		return new CodeListResponse(
			results.size(),
			results.stream().map(CodeGroupResponse::of).toList()
		);
	}
}
