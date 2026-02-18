package spring.webmvc.domain.repository.cache;

public interface TokenCacheRepository {
	void addRefreshToken(Long userId, String refreshToken);

	String getRefreshToken(Long userId, String refreshToken);

	void removeRefreshToken(Long userId, String refreshToken);
}
