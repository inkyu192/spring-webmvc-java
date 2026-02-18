package spring.webmvc.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import spring.webmvc.application.dto.query.UserQuery;
import spring.webmvc.application.dto.result.UserCredentialResult;
import spring.webmvc.domain.model.entity.User;
import spring.webmvc.domain.model.entity.UserCredential;
import spring.webmvc.domain.model.enums.Gender;
import spring.webmvc.domain.model.vo.Email;
import spring.webmvc.domain.model.vo.Phone;
import spring.webmvc.domain.repository.UserCredentialRepository;
import spring.webmvc.domain.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserCredentialRepository userCredentialRepository;

	private UserService userService;

	private User user;
	private Email email;
	private Phone phone;
	private UserCredential userCredential;

	@BeforeEach
	void setUp() {
		userService = new UserService(userRepository, userCredentialRepository);

		email = Email.create("test@example.com");
		phone = Phone.create("010-1234-5678");
		user = User.create(
			"홍길동",
			phone,
			Gender.MALE,
			LocalDate.of(1990, 1, 1),
			null
		);
		userCredential = UserCredential.create(
			user,
			email,
			"encodedPassword"
		);
	}

	@Test
	@DisplayName("회원 목록 조회")
	void findUsers() {
		Pageable pageable = PageRequest.of(0, 10);
		Instant createdFrom = Instant.now();
		Instant createdTo = Instant.now();
		UserQuery query = new UserQuery(
			pageable,
			phone,
			"홍길동",
			createdFrom,
			createdTo
		);
		Page<User> page = new PageImpl<>(List.of(user));

		when(userRepository.findAllWithOffsetPage(pageable, phone, "홍길동", createdFrom, createdTo)).thenReturn(page);

		Page<User> result = userService.findUsers(query);

		assertThat(result).isEqualTo(page);
	}

	@Test
	@DisplayName("회원 상세 조회")
	void findUserDetail() {
		Long userId = 1L;

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(userCredentialRepository.findByUserId(userId)).thenReturn(Optional.of(userCredential));

		UserCredentialResult result = userService.findUserDetail(userId);

		assertThat(result.user()).isEqualTo(user);
		assertThat(result.userCredential()).isEqualTo(userCredential);
	}
}
