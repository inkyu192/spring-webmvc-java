package com.toy.shop.business.item.controller;

import com.toy.shop.business.item.dto.request.ItemSaveRequest;
import com.toy.shop.business.item.dto.request.ItemUpdateRequest;
import com.toy.shop.business.item.dto.response.ItemResponse;
import com.toy.shop.business.item.service.ItemService;
import com.toy.shop.common.dto.ApiResponseDto;
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

        return new ApiResponseDto<>(responseDto);
    }

    @GetMapping
    public Object items(@RequestParam(required = false) Long categoryId,
                        @RequestParam(required = false) String name,
                        Pageable pageable) {
        Page<ItemResponse> page = itemService.items(categoryId, name, pageable);

        return new ApiResponseDto<>(page);
    }

    @GetMapping("/{id}")
    public Object item(@PathVariable Long id) {
        ItemResponse responseDto = itemService.item(id);

        return new ApiResponseDto<>(responseDto);
    }

    @PatchMapping("/{id}")
    public Object updateItem(@PathVariable Long id,
                             @RequestBody @Valid ItemUpdateRequest itemUpdateRequest) {
        ItemResponse responseDto = itemService.updateItem(id, itemUpdateRequest);

        return new ApiResponseDto<>(responseDto);
    }

    @DeleteMapping("/{id}")
    public Object deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);

        return new ApiResponseDto<>();
    }
}
