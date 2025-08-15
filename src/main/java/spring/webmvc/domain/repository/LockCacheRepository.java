package spring.webmvc.domain.repository;

public interface LockCacheRepository {
	Boolean tryLock(String method, String uri, String hash);
}
