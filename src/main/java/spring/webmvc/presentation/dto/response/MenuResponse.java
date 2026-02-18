package spring.webmvc.presentation.dto.response;

import java.util.List;

import spring.webmvc.application.dto.result.MenuResult;

public record MenuResponse(
	Long id,
	String name,
	String path,
	List<MenuResponse> children
) {
	public static MenuResponse of(MenuResult result) {
		return new MenuResponse(
			result.id(),
			result.name(),
			result.path(),
			result.children().stream().map(MenuResponse::of).toList()
		);
	}
}
