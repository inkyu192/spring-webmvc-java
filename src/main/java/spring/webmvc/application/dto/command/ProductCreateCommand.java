package spring.webmvc.application.dto.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.enums.Category;

@Getter
@RequiredArgsConstructor
public class ProductCreateCommand {
	private final Category category;
	private final String name;
	private final String description;
	private final int price;
	private final int quantity;
}
