package spring.web.java.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.model.entity.Item;
import spring.web.java.domain.repository.ItemRepository;
import spring.web.java.presentation.dto.request.ItemSaveRequest;
import spring.web.java.presentation.dto.response.ItemResponse;
import spring.web.java.presentation.exception.BaseException;
import spring.web.java.presentation.exception.ResponseMessage;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	@Transactional
	public ItemResponse saveItem(ItemSaveRequest itemSaveRequest) {
		Item item = Item.create(
			itemSaveRequest.name(),
			itemSaveRequest.description(),
			itemSaveRequest.price(),
			itemSaveRequest.quantity(),
			itemSaveRequest.category()
		);

		return new ItemResponse(itemRepository.save(item));
	}

	public Page<ItemResponse> findItems(Pageable pageable, String name) {
		return itemRepository.findAll(pageable, name).map(ItemResponse::new);
	}

	public ItemResponse findItem(Long id) {
		return itemRepository.findById(id)
			.map(ItemResponse::new)
			.orElseThrow(() -> new BaseException(ResponseMessage.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

	@Transactional
	public ItemResponse putItem(Long id, ItemSaveRequest itemSaveRequest) {
		Item item = itemRepository.findById(id)
			.map(findItem -> {
				findItem.update(
					itemSaveRequest.name(),
					itemSaveRequest.description(),
					itemSaveRequest.price(),
					itemSaveRequest.quantity(),
					itemSaveRequest.category()
				);

				return findItem;
			})
			.orElseGet(() -> itemRepository.save(
				Item.create(
					itemSaveRequest.name(),
					itemSaveRequest.description(),
					itemSaveRequest.price(),
					itemSaveRequest.quantity(),
					itemSaveRequest.category()
				)
			));

		return new ItemResponse(item);
	}

	@Transactional
	public void deleteItem(Long id) {
		itemRepository.deleteById(id);
	}
}
