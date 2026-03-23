package spring.webmvc.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CurationCategory {
	HOME("홈"),
	CATEGORY("카테고리"),
	EVENT("이벤트"),
	PROMOTION("프로모션"),
	MY_PAGE("마이페이지"),
	;

	private final String description;

	public String getTranslationCode() {
		return "CurationCategory." + name();
	}
}
