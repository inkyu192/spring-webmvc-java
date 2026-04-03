package spring.webmvc.domain.repository.cache;

public interface TokenCacheRepository {
	void setRefreshToken(Long userId, String deviceId, String refreshToken);

	String getRefreshToken(Long userId, String deviceId);

	void removeRefreshToken(Long userId, String deviceId);
}
