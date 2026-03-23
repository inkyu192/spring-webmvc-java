package spring.webmvc.application.dto.result;

import java.util.List;

public record CodeGroupResult(
	String name,
	String label,
	List<CodeResult> codes
) {
}
