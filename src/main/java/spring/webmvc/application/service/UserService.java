package spring.webmvc.application.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.query.UserQuery;
import spring.webmvc.application.dto.result.UserCredentialResult;
import spring.webmvc.domain.model.entity.User;
import spring.webmvc.domain.model.entity.UserCredential;
import spring.webmvc.domain.repository.UserCredentialRepository;
import spring.webmvc.domain.repository.UserRepository;
import spring.webmvc.infrastructure.exception.NotFoundEntityException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final UserCredentialRepository userCredentialRepository;

	public Page<User> findUsers(UserQuery query) {
		return userRepository.findAllWithOffsetPage(
			query.pageable(),
			query.phone(),
			query.name(),
			query.createdFrom(),
			query.createdTo()
		);
	}

	public UserCredentialResult findUserDetail(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new NotFoundEntityException(User.class, id));

		UserCredential credential = userCredentialRepository.findByUserId(id)
			.orElse(null);

		return new UserCredentialResult(user, credential, List.of());
	}
}
