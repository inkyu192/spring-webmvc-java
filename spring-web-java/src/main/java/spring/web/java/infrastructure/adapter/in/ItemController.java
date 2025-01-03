package spring.web.java.infrastructure.adapter.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import spring.web.java.application.port.in.ItemServicePort;
import spring.web.java.dto.request.ItemSaveRequest;
import spring.web.java.dto.response.ItemResponse;

@RestController
@RequestMapping("item")
@RequiredArgsConstructor
public class ItemController {

	private final ItemServicePort itemService;

	@PostMapping
	public ItemResponse saveItem(@RequestBody @Valid ItemSaveRequest itemSaveRequest) {
		return itemService.saveItem(itemSaveRequest);
	}

	@GetMapping
	public Page<ItemResponse> findItems(Pageable pageable, @RequestParam(required = false) String name) {
		return itemService.findItems(pageable, name);
	}

	@GetMapping("{id}")
	public ItemResponse findItem(@PathVariable Long id) {
		return itemService.findItem(id);
	}

	@PutMapping("{id}")
	public ItemResponse updateItem(@PathVariable Long id, @RequestBody @Valid ItemSaveRequest itemSaveRequest) {
		return itemService.updateItem(id, itemSaveRequest);
	}

	@DeleteMapping("{id}")
	public void deleteItem(@PathVariable Long id) {
		itemService.deleteItem(id);
	}
}
