package spring.webmvc.domain.model.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
	ORDER,
	CONFIRM,
	CANCEL,
	;

	@JsonCreator
	public static OrderStatus of(String name) {
		return Arrays.stream(values())
			.filter(status -> status.name().equals(name))
			.findFirst()
			.orElse(null);
	}
}
