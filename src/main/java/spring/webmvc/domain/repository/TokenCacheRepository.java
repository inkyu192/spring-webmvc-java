package spring.webmvc.domain.repository;

public interface TokenCacheRepository {
	void setRefreshToken(Long memberId, String refreshToken);
	String getRefreshToken(Long memberId);
}
