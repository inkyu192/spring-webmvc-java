package spring.webmvc.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductCategory {
	TRANSPORT("교통수단"),
	ACCOMMODATION("숙박"),
	;

	private final String description;

	public String getTranslationCode() {
		return "ProductCategory." + name();
	}
}
