package spring.webmvc.domain.repository.cache;

public interface RequestLockCacheRepository {
	boolean tryLock(String method, String uri, String hash);
}
