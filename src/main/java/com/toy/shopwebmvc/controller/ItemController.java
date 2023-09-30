package com.toy.shopwebmvc.controller;

import com.toy.shopwebmvc.dto.request.ItemSaveRequest;
import com.toy.shopwebmvc.dto.request.ItemUpdateRequest;
import com.toy.shopwebmvc.dto.response.ItemResponse;
import com.toy.shopwebmvc.service.ItemService;
import com.toy.shopwebmvc.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public Object saveItem(@RequestBody @Valid ItemSaveRequest itemSaveRequest) {
        ItemResponse responseDto = itemService.saveItem(itemSaveRequest);

        return new ApiResponse<>(responseDto);
    }

    @GetMapping
    public Object items(@RequestParam(required = false) Long categoryId,
                        @RequestParam(required = false) String name,
                        Pageable pageable) {
        Page<ItemResponse> page = itemService.items(categoryId, name, pageable);

        return new ApiResponse<>(page);
    }

    @GetMapping("/{id}")
    public Object item(@PathVariable Long id) {
        ItemResponse responseDto = itemService.item(id);

        return new ApiResponse<>(responseDto);
    }

    @PatchMapping("/{id}")
    public Object updateItem(@PathVariable Long id,
                             @RequestBody @Valid ItemUpdateRequest itemUpdateRequest) {
        ItemResponse responseDto = itemService.updateItem(id, itemUpdateRequest);

        return new ApiResponse<>(responseDto);
    }

    @DeleteMapping("/{id}")
    public Object deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);

        return new ApiResponse<>();
    }
}
