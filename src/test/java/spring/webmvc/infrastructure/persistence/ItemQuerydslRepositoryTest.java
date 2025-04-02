package spring.webmvc.infrastructure.persistence;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.querydsl.jpa.impl.JPAQueryFactory;

import spring.webmvc.domain.model.entity.Item;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.infrastructure.config.DataJpaTestConfig;

@DataJpaTest
@Import(DataJpaTestConfig.class)
class ItemQuerydslRepositoryTest {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	@Autowired
	private ItemJpaRepository itemJpaRepository;

	private ItemQuerydslRepository itemQuerydslRepository;

	@BeforeEach
	void setUp() {
		itemQuerydslRepository = new ItemQuerydslRepository(jpaQueryFactory);
	}

	@Test
	@DisplayName("findAll 은 name 을 필터링하고 페이징 되어서 조회된다")
	void findAllUsingQueryDsl() {
		// Given
		List<Item> request = List.of(
			Item.create("이름1", "설명1", 150, 1, Category.ROLE_BOOK),
			Item.create("이름2", "설명2", 160, 2, Category.ROLE_BOOK),
			Item.create("이름3", "설명3", 170, 3, Category.ROLE_BOOK),
			Item.create("이름4", "설명4", 180, 4, Category.ROLE_TICKET),
			Item.create("이5", "설명5", 190, 5, Category.ROLE_TICKET)
		);
		itemJpaRepository.saveAll(request);

		Pageable pageable = PageRequest.of(0, 3);
		String name = "이름";

		// When
		Page<Item> response = itemQuerydslRepository.findAll(pageable, name);

		// Then
		Assertions.assertThat(response.getNumber()).isEqualTo(pageable.getPageNumber());
		Assertions.assertThat(response.getSize()).isEqualTo(pageable.getPageSize());
		Assertions.assertThat(response.getTotalElements())
			.isEqualTo(request.stream().map(Item::getName).filter(s -> s.contains(name)).count());
	}
}
