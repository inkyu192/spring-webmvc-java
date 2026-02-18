package spring.webmvc.domain.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.webmvc.domain.model.entity.User;
import spring.webmvc.domain.model.vo.Phone;

public interface UserRepository {
	Optional<User> findById(Long id);

	Page<User> findAllWithOffsetPage(
		Pageable pageable,
		Phone phone,
		String name,
		Instant createdFrom,
		Instant createdTo
	);

	User save(User user);

	boolean existsByPhone(Phone phone);
}
