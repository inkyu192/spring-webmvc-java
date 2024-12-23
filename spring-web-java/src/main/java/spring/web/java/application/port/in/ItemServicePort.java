package spring.web.java.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.web.java.dto.request.ItemSaveRequest;
import spring.web.java.dto.response.ItemResponse;

public interface ItemServicePort {

	ItemResponse saveItem(ItemSaveRequest itemSaveRequest);

	Page<ItemResponse> findItems(Pageable pageable, String name);

	ItemResponse findItem(Long id);

	ItemResponse updateItem(Long id, ItemSaveRequest itemSaveRequest);

	void deleteItem(Long id);
}
