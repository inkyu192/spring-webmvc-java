package spring.web.java.application.service;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import spring.web.java.domain.model.entity.Item;
import spring.web.java.domain.model.enums.Category;
import spring.web.java.domain.repository.ItemRepository;
import spring.web.java.presentation.dto.response.ItemResponse;
import spring.web.java.presentation.dto.request.ItemSaveRequest;
import spring.web.java.presentation.exception.DomainException;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

	@InjectMocks
	private ItemService itemService;

	@Mock
	private ItemRepository itemRepository;

	@Test
	@DisplayName("saveItem은 한개를 저장한다")
	void saveItem() {
		// Given
		ItemSaveRequest request = new ItemSaveRequest("item", "description", 1000, 10, Category.ROLE_BOOK);
		Item item = Item.create("item", "description", 1000, 10, Category.ROLE_BOOK);

		Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item);

		// When
		ItemResponse response = itemService.saveItem(request);

		// Then
		Assertions.assertThat(request.name()).isEqualTo(response.name());
		Assertions.assertThat(request.description()).isEqualTo(response.description());
		Assertions.assertThat(request.price()).isEqualTo(response.price());
		Assertions.assertThat(request.quantity()).isEqualTo(response.quantity());
	}

	@Test
	@DisplayName("findItems는 여러개를 조회한다")
	void findItems() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		String name = "Item";
		List<Item> items = List.of(
			Item.create("item1", "description", 1000, 10, Category.ROLE_BOOK),
			Item.create("item2", "description", 2000, 20, Category.ROLE_TICKET),
			Item.create("item3", "description", 3000, 30, Category.ROLE_BOOK)
		);
		Page<Item> page = new PageImpl<>(items, pageable, items.size());

		Mockito.when(itemRepository.findAll(pageable, name)).thenReturn(page);

		// When
		Page<ItemResponse> response = itemService.findItems(pageable, name);

		// Then
		Assertions.assertThat(response.getTotalElements()).isEqualTo(items.size());
	}

	@Test
	@DisplayName("findItem은 데이터가 있을경우 조회한다")
	void findItem_exist_shouldFind() {
		// Given
		Long itemId = 1L;
		Item item = Item.create("item", "description", 1000, 10, Category.ROLE_BOOK);

		Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

		// When
		ItemResponse response = itemService.findItem(itemId);

		// Then
		Assertions.assertThat(item.getId()).isEqualTo(response.id());
		Assertions.assertThat(item.getName()).isEqualTo(response.name());
		Assertions.assertThat(item.getDescription()).isEqualTo(response.description());
		Assertions.assertThat(item.getPrice()).isEqualTo(response.price());
		Assertions.assertThat(item.getQuantity()).isEqualTo(response.quantity());
	}

	@Test
	@DisplayName("findItem은 데이터가 없을경우 DomainException을 던진다")
	void findItem_notExist_shouldThrowDomainException() {
		// Given
		Long itemId = 1L;

		Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

		// When & Then
		Assertions.assertThatThrownBy(() -> itemService.findItem(itemId)).isInstanceOf(DomainException.class);
	}

	@Test
	@DisplayName("putItem은 데이터가 있을 경우 수정된다")
	void putItem_exist_shouldUpdate() {
		// Given
		Long itemId = 1L;
		Item oldItem = Item.create("oldItem", "description", 1000, 10, Category.ROLE_BOOK);
		ItemSaveRequest request = new ItemSaveRequest("newItem", "description", 2000, 20, Category.ROLE_BOOK);

		Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(oldItem));

		// When
		ItemResponse response = itemService.putItem(itemId, request);

		// Then
		Assertions.assertThat(request.name()).isEqualTo(response.name());
		Assertions.assertThat(request.description()).isEqualTo(response.description());
		Assertions.assertThat(request.price()).isEqualTo(response.price());
		Assertions.assertThat(request.quantity()).isEqualTo(response.quantity());

		Mockito.verify(itemRepository, Mockito.never()).save(Mockito.any());
	}

	@Test
	@DisplayName("putItem은 데이터가 없을 경우 저장한다")
	void putItem_notExist_shouldCreate() {
		// Given
		Long itemId = 1L;
		ItemSaveRequest request = new ItemSaveRequest("NewItem", "description", 2000, 20, Category.ROLE_BOOK);
		Item newItem = Item.create(
			request.name(),
			request.description(),
			request.price(),
			request.quantity(),
			request.category()
		);

		Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
		Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(newItem);

		// When
		ItemResponse response = itemService.putItem(itemId, request);

		// Then
		Assertions.assertThat(request.name()).isEqualTo(response.name());
		Assertions.assertThat(request.description()).isEqualTo(response.description());
		Assertions.assertThat(request.price()).isEqualTo(response.price());
		Assertions.assertThat(request.quantity()).isEqualTo(response.quantity());

		Mockito.verify(itemRepository, Mockito.times(1)).save(Mockito.any());
	}

	@Test
	@DisplayName("deleteItem은 데이터를 삭제한다")
	void deleteItem() {
		// Given
		Long itemId = 1L;

		Mockito.doNothing().when(itemRepository).deleteById(itemId);

		// When
		itemService.deleteItem(itemId);

		// Then
		Mockito.verify(itemRepository, Mockito.times(1)).deleteById(itemId);
	}
}
