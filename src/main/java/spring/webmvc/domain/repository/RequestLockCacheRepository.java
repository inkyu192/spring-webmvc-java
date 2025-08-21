package spring.webmvc.domain.repository;

public interface RequestLockCacheRepository {
	Boolean tryLock(String method, String uri, String hash);
}
