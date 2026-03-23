package spring.webmvc.presentation.dto.response;

import java.util.List;

import spring.webmvc.application.dto.result.CodeGroupResult;

public record CodeGroupResponse(
	String name,
	String label,
	List<CodeResponse> codes
) {
	public static CodeGroupResponse of(CodeGroupResult result) {
		return new CodeGroupResponse(
			result.name(),
			result.label(),
			result.codes().stream().map(CodeResponse::of).toList()
		);
	}
}
