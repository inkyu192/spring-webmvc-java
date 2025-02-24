package spring.web.java.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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

import lombok.RequiredArgsConstructor;
import spring.web.java.application.service.ItemService;
import spring.web.java.presentation.dto.request.ItemSaveRequest;
import spring.web.java.presentation.dto.response.ItemResponse;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@PostMapping
	@PreAuthorize("hasAuthority('ITEM_CREATE')")
	@ResponseStatus(HttpStatus.CREATED)
	public ItemResponse saveItem(@RequestBody @Validated ItemSaveRequest itemSaveRequest) {
		return itemService.saveItem(itemSaveRequest);
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ITEM_READ')")
	public Page<ItemResponse> findItems(
		@PageableDefault Pageable pageable,
		@RequestParam(required = false) String name
	) {
		return itemService.findItems(pageable, name);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ITEM_READ')")
	public ItemResponse findItem(@PathVariable Long id) {
		return itemService.findItem(id);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ITEM_UPDATE')")
	public ItemResponse putItem(@PathVariable Long id, @RequestBody @Validated ItemSaveRequest itemSaveRequest) {
		return itemService.putItem(id, itemSaveRequest);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ITEM_DELETE')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteItem(@PathVariable Long id) {
		itemService.deleteItem(id);
	}
}
