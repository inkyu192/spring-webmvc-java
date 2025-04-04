package spring.webmvc.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import spring.webmvc.application.service.ItemService;
import spring.webmvc.presentation.dto.request.ItemSaveRequest;
import spring.webmvc.presentation.dto.response.ItemResponse;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@PostMapping
	@PreAuthorize("hasAuthority('ITEM_WRITER')")
	@ResponseStatus(HttpStatus.CREATED)
	public ItemResponse saveItem(@RequestBody @Validated ItemSaveRequest itemSaveRequest) {
		return itemService.saveItem(itemSaveRequest);
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ITEM_READER')")
	public Page<ItemResponse> findItems(
		@PageableDefault Pageable pageable,
		@RequestParam(required = false) String name
	) {
		return itemService.findItems(pageable, name);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ITEM_READER')")
	public ItemResponse findItem(@PathVariable Long id) {
		return itemService.findItem(id);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ITEM_WRITER')")
	public ResponseEntity<ItemResponse> putItem(
		@PathVariable Long id,
		@RequestBody @Validated ItemSaveRequest itemSaveRequest
	) {
		Pair<Boolean, ItemResponse> pair = itemService.putItem(id, itemSaveRequest);
		Boolean isNew = pair.getFirst();
		ItemResponse itemResponse = pair.getSecond();
		HttpStatus status = isNew ? HttpStatus.CREATED : HttpStatus.OK;

		return ResponseEntity.status(status).body(itemResponse);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ITEM_WRITER')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteItem(@PathVariable Long id) {
		itemService.deleteItem(id);
	}
}
