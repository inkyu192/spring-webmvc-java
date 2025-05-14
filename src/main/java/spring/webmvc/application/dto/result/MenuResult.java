package spring.webmvc.application.dto.result;

import java.util.List;

import spring.webmvc.domain.model.entity.Menu;

public record MenuResult(
	Long id,
	String name,
	String path,
	List<MenuResult> children
) {
	public MenuResult(Menu menu) {
		this(
			menu.getId(),
			menu.getName(),
			menu.getPath(),
			menu.getChildren().stream()
				.map(MenuResult::new)
				.toList()
		);
	}
}
