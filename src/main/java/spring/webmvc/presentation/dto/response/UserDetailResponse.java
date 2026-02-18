package spring.webmvc.presentation.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import spring.webmvc.domain.model.entity.User;
import spring.webmvc.domain.model.entity.UserCredential;
import spring.webmvc.domain.model.entity.UserOAuth;
import spring.webmvc.domain.model.enums.Gender;

public record UserDetailResponse(
	Long id,
	String name,
	String phone,
	Gender gender,
	LocalDate birthday,
	UserCredentialResponse credential,
	List<UserOAuthResponse> oAuths,
	Instant createdAt
) {
	public static UserDetailResponse of(
		User user,
		UserCredential credential,
		List<UserOAuth> oAuths
	) {
		return new UserDetailResponse(
			user.getId(),
			user.getName(),
			user.getPhone().getValue(),
			user.getGender(),
			user.getBirthday(),
			UserCredentialResponse.of(credential),
			oAuths.stream().map(UserOAuthResponse::of).toList(),
			user.getCreatedAt()
		);
	}
}
