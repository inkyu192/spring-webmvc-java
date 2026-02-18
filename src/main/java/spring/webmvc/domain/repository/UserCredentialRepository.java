package spring.webmvc.domain.repository;

import java.util.Optional;

import spring.webmvc.domain.model.entity.UserCredential;
import spring.webmvc.domain.model.vo.Email;

public interface UserCredentialRepository {
	Optional<UserCredential> findByEmail(Email email);

	Optional<UserCredential> findByUserId(Long userId);

	boolean existsByEmail(Email email);

	UserCredential save(UserCredential userCredential);
}
