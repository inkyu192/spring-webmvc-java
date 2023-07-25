package com.toy.shop.business.item.service;

import com.toy.shop.business.item.dto.request.ItemSaveRequest;
import com.toy.shop.business.item.dto.request.ItemUpdateRequest;
import com.toy.shop.business.item.dto.response.ItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {

    ItemResponse saveItem(ItemSaveRequest itemSaveRequest);

    Page<ItemResponse> items(Long categoryId, String name, Pageable pageable);

    ItemResponse item(Long id);

    ItemResponse updateItem(Long id, ItemUpdateRequest itemUpdateRequest);

    void deleteItem(Long id);
}
