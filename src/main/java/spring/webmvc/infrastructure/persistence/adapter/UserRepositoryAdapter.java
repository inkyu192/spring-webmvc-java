package spring.webmvc.infrastructure.persistence.adapter;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.User;
import spring.webmvc.domain.model.vo.Phone;
import spring.webmvc.domain.repository.UserRepository;
import spring.webmvc.infrastructure.persistence.jpa.UserJpaRepository;
import spring.webmvc.infrastructure.persistence.jpa.UserQuerydslRepository;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

	private final UserJpaRepository jpaRepository;
	private final UserQuerydslRepository querydslRepository;

	@Override
	public Optional<User> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public Page<User> findAllWithOffsetPage(
		Pageable pageable,
		Phone phone,
		String name,
		Instant createdFrom,
		Instant createdTo
	) {
		return querydslRepository.findAllWithOffsetPage(pageable, phone, name, createdFrom, createdTo);
	}

	@Override
	public User save(User user) {
		return jpaRepository.save(user);
	}

	@Override
	public boolean existsByPhone(Phone phone) {
		return jpaRepository.existsByPhone(phone);
	}
}
