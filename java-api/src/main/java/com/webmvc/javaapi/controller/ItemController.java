package com.webmvc.javaapi.controller;

import com.webmvc.javaapi.dto.request.ItemSaveRequest;
import com.webmvc.javaapi.dto.response.ApiResponse;
import com.webmvc.javaapi.dto.response.ItemResponse;
import com.webmvc.javaapi.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ApiResponse<ItemResponse> saveItem(@RequestBody @Valid ItemSaveRequest itemSaveRequest) {
        ItemResponse responseDto = itemService.saveItem(itemSaveRequest);

        return new ApiResponse<>(responseDto);
    }

    @GetMapping
    public ApiResponse<Page<ItemResponse>> findItems(Pageable pageable, @RequestParam(required = false) String name) {
        Page<ItemResponse> page = itemService.findItems(pageable, name);

        return new ApiResponse<>(page);
    }

    @GetMapping("{id}")
    public ApiResponse<ItemResponse> findItem(@PathVariable Long id) {
        ItemResponse responseDto = itemService.findItem(id);

        return new ApiResponse<>(responseDto);
    }

    @PutMapping("{id}")
    public ApiResponse<ItemResponse> updateItem(
            @PathVariable Long id,
            @RequestBody @Valid ItemSaveRequest itemSaveRequest
    ) {
        ItemResponse responseDto = itemService.updateItem(id, itemSaveRequest);

        return new ApiResponse<>(responseDto);
    }

    @DeleteMapping("{id}")
    public ApiResponse<ItemResponse> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);

        return new ApiResponse<>();
    }
}
