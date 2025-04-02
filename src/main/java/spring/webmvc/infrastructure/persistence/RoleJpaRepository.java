package spring.webmvc.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.Role;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {
}
