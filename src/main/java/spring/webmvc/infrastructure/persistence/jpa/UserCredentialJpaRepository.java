package spring.webmvc.infrastructure.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.webmvc.domain.model.entity.UserCredential;

public interface UserCredentialJpaRepository extends JpaRepository<UserCredential, Long> {
	Optional<UserCredential> findByEmailValue(String email);

	boolean existsByEmailValue(String email);
}
