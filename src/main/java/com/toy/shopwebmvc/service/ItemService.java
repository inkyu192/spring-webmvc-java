package com.toy.shopwebmvc.service;

import com.toy.shopwebmvc.dto.request.ItemSaveRequest;
import com.toy.shopwebmvc.dto.request.ItemUpdateRequest;
import com.toy.shopwebmvc.dto.response.ItemResponse;
import com.toy.shopwebmvc.repository.ItemRepository;
import com.toy.shopwebmvc.domain.Item;
import com.toy.shopwebmvc.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.toy.shopwebmvc.constant.ApiResponseCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public ItemResponse saveItem(ItemSaveRequest itemSaveRequest) {
        Item item = Item.create()
                .name(itemSaveRequest.name())
                .description(itemSaveRequest.description())
                .price(itemSaveRequest.price())
                .quantity(itemSaveRequest.quantity())
                .category(itemSaveRequest.category())
                .build();

        itemRepository.save(item);

        return new ItemResponse(item);
    }

    public Page<ItemResponse> items(String name, Pageable pageable) {
        return itemRepository.findAll(name, pageable)
                .map(ItemResponse::new);
    }

    public ItemResponse item(Long id) {
        return itemRepository.findById(id)
                .map(ItemResponse::new)
                .orElseThrow(() -> new CommonException(DATA_NOT_FOUND));
    }

    @Transactional
    public ItemResponse updateItem(Long id, ItemUpdateRequest itemUpdateRequest) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new CommonException(DATA_NOT_FOUND))
                .toBuilder()
                .name(itemUpdateRequest.name())
                .description(itemUpdateRequest.description())
                .price(itemUpdateRequest.price())
                .quantity(itemUpdateRequest.quantity())
                .category(itemUpdateRequest.category())
                .build();

        return new ItemResponse(item);
    }

    @Transactional
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new CommonException(DATA_NOT_FOUND));

        itemRepository.delete(item);
    }
}
