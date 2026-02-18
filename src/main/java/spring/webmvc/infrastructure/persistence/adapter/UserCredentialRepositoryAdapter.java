package spring.webmvc.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.UserCredential;
import spring.webmvc.domain.model.vo.Email;
import spring.webmvc.domain.repository.UserCredentialRepository;
import spring.webmvc.infrastructure.persistence.jpa.UserCredentialJpaRepository;
import spring.webmvc.infrastructure.persistence.jpa.UserCredentialQuerydslRepository;

@Component
@RequiredArgsConstructor
public class UserCredentialRepositoryAdapter implements UserCredentialRepository {

	private final UserCredentialJpaRepository jpaRepository;
	private final UserCredentialQuerydslRepository querydslRepository;

	@Override
	public Optional<UserCredential> findByEmail(Email email) {
		return jpaRepository.findByEmailValue(email.getValue());
	}

	@Override
	public Optional<UserCredential> findByUserId(Long userId) {
		return querydslRepository.findByUserId(userId);
	}

	@Override
	public boolean existsByEmail(Email email) {
		return jpaRepository.existsByEmailValue(email.getValue());
	}

	@Override
	public UserCredential save(UserCredential userCredential) {
		return jpaRepository.save(userCredential);
	}
}
