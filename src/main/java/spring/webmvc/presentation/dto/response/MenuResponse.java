package spring.webmvc.presentation.dto.response;

import java.util.List;

import spring.webmvc.application.dto.result.MenuResult;

public record MenuResponse(
	Long id,
	String name,
	String path,
	List<MenuResponse> children
) {
	public MenuResponse(MenuResult menuResult) {
		this(
			menuResult.id(),
			menuResult.name(),
			menuResult.path(),
			menuResult.children().stream()
				.map(MenuResponse::new)
				.toList()
		);
	}
}
