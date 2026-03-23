package spring.webmvc.presentation.dto.response;

import spring.webmvc.application.dto.result.CodeResult;

public record CodeResponse(
	String code,
	String label
) {
	public static CodeResponse of(CodeResult result) {
		return new CodeResponse(
			result.code(),
			result.label()
		);
	}
}
