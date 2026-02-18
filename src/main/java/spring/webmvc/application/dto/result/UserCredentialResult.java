package spring.webmvc.application.dto.result;

import java.util.List;

import spring.webmvc.domain.model.entity.User;
import spring.webmvc.domain.model.entity.UserCredential;
import spring.webmvc.domain.model.entity.UserOAuth;

public record UserCredentialResult(
	User user,
	UserCredential userCredential,
	List<UserOAuth> oAuths
) {
}
