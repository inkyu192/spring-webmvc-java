package spring.web.java.domain.item.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import spring.web.java.domain.item.Item;

@DataJpaTest
class ItemRepositoryTest {

	@Autowired
	private ItemRepository itemRepository;

	@Test
	@DisplayName("findAllUsingJpql은 name을 필터링하고 페이징 되어서 조회된다")
	void findAllUsingJpql() {
		// Given
		List<Item> request = List.of(
			Item.create("이름1", "설명1", 150, 1, Item.Category.ROLE_BOOK),
			Item.create("이름2", "설명2", 160, 2, Item.Category.ROLE_BOOK),
			Item.create("이름3", "설명3", 170, 3, Item.Category.ROLE_BOOK),
			Item.create("이름4", "설명4", 180, 4, Item.Category.ROLE_TICKET),
			Item.create("이5", "설명5", 190, 5, Item.Category.ROLE_TICKET)
		);
		itemRepository.saveAll(request);

		Pageable pageable = PageRequest.of(0, 3);
		String name = "이름";

		// When
		Page<Item> response = itemRepository.findAllUsingJpql(pageable, name);

		// Then
		Assertions.assertThat(response.getNumber()).isEqualTo(pageable.getPageNumber());
		Assertions.assertThat(response.getSize()).isEqualTo(pageable.getPageSize());
		Assertions.assertThat(response.getTotalElements())
			.isEqualTo(request.stream().map(Item::getName).filter(s -> s.contains(name)).count());
	}

	@Test
	@DisplayName("findAllUsingQueryDsl은 name을 필터링하고 페이징 되어서 조회된다")
	void findAllUsingQueryDsl() {
		// Given
		List<Item> request = List.of(
			Item.create("이름1", "설명1", 150, 1, Item.Category.ROLE_BOOK),
			Item.create("이름2", "설명2", 160, 2, Item.Category.ROLE_BOOK),
			Item.create("이름3", "설명3", 170, 3, Item.Category.ROLE_BOOK),
			Item.create("이름4", "설명4", 180, 4, Item.Category.ROLE_TICKET),
			Item.create("이5", "설명5", 190, 5, Item.Category.ROLE_TICKET)
		);
		itemRepository.saveAll(request);

		Pageable pageable = PageRequest.of(0, 3);
		String name = "이름";

		// When
		Page<Item> response = itemRepository.findAllUsingQueryDsl(pageable, name);

		// Then
		Assertions.assertThat(response.getNumber()).isEqualTo(pageable.getPageNumber());
		Assertions.assertThat(response.getSize()).isEqualTo(pageable.getPageSize());
		Assertions.assertThat(response.getTotalElements())
			.isEqualTo(request.stream().map(Item::getName).filter(s -> s.contains(name)).count());
	}
}
