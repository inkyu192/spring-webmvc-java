package spring.web.java.domain.repository;

public interface RequestLockRepository {

	boolean setIfAbsent(Long memberId, String method, String uri);
}
