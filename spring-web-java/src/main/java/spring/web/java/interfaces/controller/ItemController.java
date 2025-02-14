package spring.web.java.interfaces.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import spring.web.java.application.service.ItemService;
import spring.web.java.interfaces.dto.request.ItemSaveRequest;
import spring.web.java.interfaces.dto.response.ItemResponse;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ItemResponse saveItem(@RequestBody @Valid ItemSaveRequest itemSaveRequest) {
		return itemService.saveItem(itemSaveRequest);
	}

	@GetMapping
	public Page<ItemResponse> findItems(Pageable pageable, @RequestParam(required = false) String name) {
		return itemService.findItems(pageable, name);
	}

	@GetMapping("/{id}")
	public ItemResponse findItem(@PathVariable Long id) {
		return itemService.findItem(id);
	}

	@PutMapping("/{id}")
	public ItemResponse putItem(@PathVariable Long id, @RequestBody @Valid ItemSaveRequest itemSaveRequest) {
		return itemService.putItem(id, itemSaveRequest);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteItem(@PathVariable Long id) {
		itemService.deleteItem(id);
	}
}
