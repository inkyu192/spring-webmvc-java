package spring.webmvc.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.User;
import spring.webmvc.domain.model.vo.Phone;

public interface UserJpaRepository extends JpaRepository<User, Long> {
	boolean existsByPhone(Phone phone);
}
