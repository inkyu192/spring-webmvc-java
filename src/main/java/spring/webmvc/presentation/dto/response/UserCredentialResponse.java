package spring.webmvc.presentation.dto.response;

import java.time.Instant;

import spring.webmvc.domain.model.entity.UserCredential;

public record UserCredentialResponse(
	String email,
	Instant verifiedAt
) {
	public static UserCredentialResponse of(UserCredential credential) {
		return new UserCredentialResponse(
			credential.getEmail().getValue(),
			credential.getVerifiedAt()
		);
	}
}
