package spring.webmvc.presentation.dto.response;

import spring.webmvc.domain.model.entity.UserOAuth;
import spring.webmvc.domain.model.enums.OauthProvider;

public record UserOAuthResponse(
	OauthProvider provider,
	String oauthUserId
) {
	public static UserOAuthResponse of(UserOAuth oauth) {
		return new UserOAuthResponse(
			oauth.getOauthProvider(),
			oauth.getOauthUserId()
		);
	}
}
