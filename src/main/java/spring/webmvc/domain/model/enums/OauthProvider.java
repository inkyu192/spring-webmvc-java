package spring.webmvc.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OauthProvider {
	GOOGLE("구글"),
	KAKAO("카카오"),
	APPLE("애플"),
	;

	private final String description;

	public String getTranslationCode() {
		return "OauthProvider." + name();
	}
}
