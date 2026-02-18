package spring.webmvc.presentation.dto.response;

import java.time.Instant;
import java.time.LocalDate;

import spring.webmvc.domain.model.entity.User;
import spring.webmvc.domain.model.enums.Gender;

public record UserSummaryResponse(
	Long id,
	String name,
	String phone,
	Gender gender,
	LocalDate birthDate,
	Instant createdAt
) {
	public static UserSummaryResponse of(User user) {
		return new UserSummaryResponse(
			user.getId(),
			user.getName(),
			user.getPhone().getValue(),
			user.getGender(),
			user.getBirthday(),
			user.getCreatedAt()
		);
	}
}
