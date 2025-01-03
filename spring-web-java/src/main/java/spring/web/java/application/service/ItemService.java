package spring.web.java.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.web.java.application.port.in.ItemServicePort;
import spring.web.java.application.port.out.ItemRepositoryPort;
import spring.web.java.common.ResponseMessage;
import spring.web.java.domain.Item;
import spring.web.java.dto.request.ItemSaveRequest;
import spring.web.java.dto.response.ItemResponse;
import spring.web.java.infrastructure.configuration.exception.DomainException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService implements ItemServicePort {

	private final ItemRepositoryPort itemRepository;

	@Override
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

	@Override
	public Page<ItemResponse> findItems(Pageable pageable, String name) {
		return itemRepository.findAll(pageable, name).map(ItemResponse::new);
	}

	@Override
	public ItemResponse findItem(Long id) {
		return itemRepository.findById(id)
			.map(ItemResponse::new)
			.orElseThrow(() -> new DomainException(ResponseMessage.DATA_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

	@Override
	@Transactional
	public ItemResponse updateItem(Long id, ItemSaveRequest itemSaveRequest) {
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
			.orElseGet(() -> Item.create(
				itemSaveRequest.name(),
				itemSaveRequest.description(),
				itemSaveRequest.price(),
				itemSaveRequest.quantity(),
				itemSaveRequest.category()
			));

		return new ItemResponse(item);
	}

	@Override
	@Transactional
	public void deleteItem(Long id) {
		itemRepository.deleteById(id);
	}
}
