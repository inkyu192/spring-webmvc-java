package spring.web.java.domain.item.serivce;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import spring.web.java.domain.item.Item;
import spring.web.java.domain.item.dto.ItemResponse;
import spring.web.java.domain.item.repository.ItemRepository;
import spring.web.java.global.TestConfig;

@DataJpaTest
@Import(TestConfig.class)
class ItemServiceTest {

	ItemRepository itemRepository;
	ItemService itemService;

	@Autowired
	public ItemServiceTest(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
		this.itemService = new ItemService(itemRepository);
	}

	@Test
	void findItem() {
		// Given
		Item savedItem = itemRepository.save(
			Item.create("이름", "설명", 15000, 10, Item.Category.ROLE_BOOK)
		);

		// When
		ItemResponse findItem = itemService.findItem(savedItem.getId());

		// Then
		Assertions.assertThat(savedItem.getId()).isEqualTo(findItem.id());
	}
}
