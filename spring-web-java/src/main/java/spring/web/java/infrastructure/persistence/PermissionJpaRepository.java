package spring.web.java.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.web.java.domain.model.entity.Permission;

public interface PermissionJpaRepository extends JpaRepository<Permission, Long> {
}
