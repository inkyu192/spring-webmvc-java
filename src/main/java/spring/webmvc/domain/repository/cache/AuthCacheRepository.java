package spring.webmvc.domain.repository.cache;

import spring.webmvc.domain.model.vo.Email;

public interface AuthCacheRepository {
	void setJoinVerifyToken(String token, Email email);

	String getJoinVerifyToken(String token);

	void deleteJoinVerifyToken(String token);

	void setPasswordResetToken(String token, Email email);

	String getPasswordResetToken(String token);

	void deletePasswordResetToken(String token);
}
