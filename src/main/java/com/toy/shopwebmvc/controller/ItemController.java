package com.toy.shopwebmvc.controller;

import com.toy.shopwebmvc.dto.request.ItemSaveRequest;
import com.toy.shopwebmvc.dto.request.ItemUpdateRequest;
import com.toy.shopwebmvc.dto.response.ItemResponse;
import com.toy.shopwebmvc.dto.response.MemberResponse;
import com.toy.shopwebmvc.service.ItemService;
import com.toy.shopwebmvc.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ApiResponse<ItemResponse> saveItem(@RequestBody @Valid ItemSaveRequest itemSaveRequest) {
        ItemResponse responseDto = itemService.saveItem(itemSaveRequest);

        return new ApiResponse<>(responseDto);
    }

    @GetMapping
    public ApiResponse<Page<ItemResponse>> items(Pageable pageable, @RequestParam(required = false) String name) {
        Page<ItemResponse> page = itemService.items(name, pageable);

        return new ApiResponse<>(page);
    }

    @GetMapping("/{id}")
    public ApiResponse<ItemResponse> item(@PathVariable Long id) {
        ItemResponse responseDto = itemService.item(id);

        return new ApiResponse<>(responseDto);
    }

    @PatchMapping("/{id}")
    public ApiResponse<ItemResponse> updateItem(@PathVariable Long id,
                             @RequestBody @Valid ItemUpdateRequest itemUpdateRequest) {
        ItemResponse responseDto = itemService.updateItem(id, itemUpdateRequest);

        return new ApiResponse<>(responseDto);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ItemResponse> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);

        return new ApiResponse<>();
    }
}
