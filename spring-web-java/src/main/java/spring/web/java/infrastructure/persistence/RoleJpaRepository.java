package spring.web.java.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.web.java.domain.model.entity.Role;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {
}
