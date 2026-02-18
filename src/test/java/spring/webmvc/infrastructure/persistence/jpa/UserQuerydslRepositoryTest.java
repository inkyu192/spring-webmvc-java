package spring.webmvc.infrastructure.persistence.jpa;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import spring.webmvc.domain.model.entity.User;
import spring.webmvc.domain.model.enums.Gender;
import spring.webmvc.domain.model.vo.Phone;
import spring.webmvc.infrastructure.config.RepositoryTest;

@RepositoryTest
class UserQuerydslRepositoryTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	private UserQuerydslRepository userQuerydslRepository;

	private User user1;
	private User user2;
	private User user3;

	@BeforeEach
	void setUp() {
		userQuerydslRepository = new UserQuerydslRepository(jpaQueryFactory);

		user1 = User.create(
			"홍길동",
			Phone.create("010-1111-1111"),
			Gender.MALE,
			LocalDate.of(1990, 1, 1),
			null
		);
		user2 = User.create(
			"김철수",
			Phone.create("010-2222-2222"),
			Gender.MALE,
			LocalDate.of(1985, 5, 15),
			null
		);
		user3 = User.create(
			"이영희",
			Phone.create("010-3333-3333"),
			Gender.FEMALE,
			LocalDate.of(1995, 12, 25),
			null
		);

		entityManager.persist(user1);
		entityManager.persist(user2);
		entityManager.persist(user3);

		entityManager.flush();
		entityManager.clear();
	}

	@Test
	@DisplayName("findAllWithOffsetPage: 전화번호 조건으로 회원을 조회한다")
	void findAllWithOffsetPageByPhone() {
		Pageable pageable = PageRequest.of(0, 10);
		Instant createdFrom = Instant.now().minus(1, ChronoUnit.DAYS);
		Instant createdTo = Instant.now().plus(1, ChronoUnit.DAYS);

		Page<User> result = userQuerydslRepository.findAllWithOffsetPage(
			pageable,
			Phone.create("010-1111-1111"),
			null,
			createdFrom,
			createdTo
		);

		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().get(0).getPhone().getValue()).isEqualTo("010-1111-1111");
	}

	@Test
	@DisplayName("findAllWithOffsetPage: 조건 없이 전체 회원을 페이징 조회한다")
	void findAllWithOffsetPageWithoutCondition() {
		Pageable pageable = PageRequest.of(0, 2);
		Instant createdFrom = Instant.now().minus(1, ChronoUnit.DAYS);
		Instant createdTo = Instant.now().plus(1, ChronoUnit.DAYS);

		Page<User> result = userQuerydslRepository.findAllWithOffsetPage(
			pageable,
			null,
			null,
			createdFrom,
			createdTo
		);

		assertThat(result.getTotalElements()).isEqualTo(3);
		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getTotalPages()).isEqualTo(2);
	}
}
