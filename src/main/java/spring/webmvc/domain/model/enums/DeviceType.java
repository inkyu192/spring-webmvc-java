package spring.webmvc.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeviceType {
	IOS("iOS"),
	ANDROID("안드로이드"),
	WEB("웹"),
	;

	private final String description;

	public String getTranslationCode() {
		return "DeviceType." + name();
	}
}
