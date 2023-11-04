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
        Item item = Item.create(
                itemSaveRequest.name(),
                itemSaveRequest.description(),
                itemSaveRequest.price(),
                itemSaveRequest.quantity(),
                itemSaveRequest.category()
        );

        itemRepository.save(item);

        return ItemResponse.create(item);
    }

    public Page<ItemResponse> findItems(String name, Pageable pageable) {
        return itemRepository.findAll(name, pageable)
                .map(ItemResponse::create);
    }

    public ItemResponse findItem(Long id) {
        return itemRepository.findById(id)
                .map(ItemResponse::create)
                .orElseThrow(() -> new CommonException(DATA_NOT_FOUND));
    }

    @Transactional
    public ItemResponse updateItem(Long id, ItemUpdateRequest itemUpdateRequest) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new CommonException(DATA_NOT_FOUND));

        item.update(
                itemUpdateRequest.name(),
                itemUpdateRequest.description(),
                itemUpdateRequest.price(),
                itemUpdateRequest.quantity(),
                itemUpdateRequest.category()
        );

        return ItemResponse.create(item);
    }

    @Transactional
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
