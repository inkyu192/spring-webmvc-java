package spring.webmvc.application.dto.query;

import java.time.Instant;

import org.springframework.data.domain.Pageable;

import spring.webmvc.domain.model.vo.Phone;

public record UserQuery(
	Pageable pageable,
	Phone phone,
	String name,
	Instant createdFrom,
	Instant createdTo
) {
	public static UserQuery create(
		Pageable pageable,
		String phone,
		String name,
		Instant createdFrom,
		Instant createdTo
	) {
		return new UserQuery(
			pageable,
			Phone.create(phone),
			name,
			createdFrom,
			createdTo
		);
	}
}
