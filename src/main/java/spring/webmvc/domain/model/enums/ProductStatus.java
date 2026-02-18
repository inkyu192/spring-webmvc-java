package spring.webmvc.domain.model.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
	PENDING("대기"),
	SELLING("판매중"),
	DISCONTINUED("중지"),
	;

	private final String description;

	@JsonCreator
	public static ProductStatus of(String name) {
		return Arrays.stream(values())
			.filter(status -> status.name().equals(name))
			.findFirst()
			.orElse(null);
	}
}
