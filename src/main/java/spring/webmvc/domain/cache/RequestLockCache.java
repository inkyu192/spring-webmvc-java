package spring.webmvc.domain.cache;

public interface RequestLockCache {
	boolean setIfAbsent(Long memberId, String method, String uri);
}
