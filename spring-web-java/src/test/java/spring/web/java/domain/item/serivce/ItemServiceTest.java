package spring.web.java.domain.item.serivce;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import spring.web.java.domain.item.Item;
import spring.web.java.domain.item.dto.ItemResponse;
import spring.web.java.domain.item.dto.ItemSaveRequest;
import spring.web.java.domain.item.repository.ItemRepository;
import spring.web.java.global.TestConfig;

@DataJpaTest
@Import(TestConfig.class)
class ItemServiceTest {

	private final ItemRepository itemRepository;
	private final ItemService itemService;

	@Autowired
	public ItemServiceTest(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
		this.itemService = new ItemService(itemRepository);
	}

	@Test
	void findItem() {
		// Given
		Item item = Item.create("이름", "설명", 15000, 10, Item.Category.ROLE_BOOK);
		Long itemId = itemRepository.save(item).getId();

		// When
		ItemResponse findItem = itemService.findItem(itemId);

		// Then
		assertThat(findItem.name()).isEqualTo(item.getName());
		assertThat(findItem.description()).isEqualTo(item.getDescription());
		assertThat(findItem.price()).isEqualTo(item.getPrice());
		assertThat(findItem.quantity()).isEqualTo(item.getQuantity());
	}

	@Test
	void putItem() {
		// Given
		Item item = Item.create("이름", "설명", 15000, 10, Item.Category.ROLE_BOOK);
		Long itemId = itemRepository.save(item).getId();

		// When
		ItemSaveRequest request = new ItemSaveRequest("이름2", "설명2", 20000, 20, Item.Category.ROLE_TICKET);
		itemService.putItem(itemId, request);

		// Then
		Item findItem = itemRepository.findById(itemId).orElseThrow(IllegalArgumentException::new);

		assertThat(findItem.getName()).isEqualTo(request.name());
		assertThat(findItem.getDescription()).isEqualTo(request.description());
		assertThat(findItem.getPrice()).isEqualTo(request.price());
		assertThat(findItem.getQuantity()).isEqualTo(request.quantity());
		assertThat(findItem.getCategory()).isEqualTo(request.category());
	}
}
