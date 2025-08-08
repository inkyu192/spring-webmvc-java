package spring.webmvc.application.dto.result;

import java.util.List;

public record MenuResult(
	Long id,
	String name,
	String path,
	List<MenuResult> children
) {
}
