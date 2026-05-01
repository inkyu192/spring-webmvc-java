package spring.webmvc.domain.model.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
	PENDING,
	SELLING,
	DISCONTINUED,
	;

	@JsonCreator
	public static ProductStatus of(String name) {
		return Arrays.stream(values())
			.filter(status -> status.name().equals(name))
			.findFirst()
			.orElse(null);
	}
}
