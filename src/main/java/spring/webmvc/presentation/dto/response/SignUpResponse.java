package spring.webmvc.presentation.dto.response;

import spring.webmvc.domain.model.entity.User;

public record SignUpResponse(
	Long id,
	String profileImage
) {
	public static SignUpResponse of(User user, String cloudfrontDomain) {
		return new SignUpResponse(
			user.getId(),
			user.getProfileImage() != null ? cloudfrontDomain + "/" + user.getProfileImage() : null
		);
	}
}
