package spring.web.java.domain.requestlock.repository;

import org.springframework.data.repository.CrudRepository;

import spring.web.java.domain.requestlock.RequestLock;

public interface RequestLockRepository extends CrudRepository<RequestLock, String> {
}
