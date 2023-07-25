package com.toy.shop.business.item.service;

import com.toy.shop.business.category.repository.CategoryRepository;
import com.toy.shop.business.item.dto.request.ItemSaveRequest;
import com.toy.shop.business.item.dto.request.ItemUpdateRequest;
import com.toy.shop.business.item.dto.response.ItemResponse;
import com.toy.shop.business.item.repository.ItemRepository;
import com.toy.shop.common.ApiResponseCode;
import com.toy.shop.domain.Category;
import com.toy.shop.domain.Item;
import com.toy.shop.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ItemResponse saveItem(ItemSaveRequest itemSaveRequest) {
        Category category = categoryRepository.findById(itemSaveRequest.categoryId())
                .orElseThrow(() -> new CommonException(ApiResponseCode.CATEGORY_NOT_FOUND));

        Item item = Item.createItem(itemSaveRequest, category);

        itemRepository.save(item);

        return new ItemResponse(item);
    }

    @Override
    public Page<ItemResponse> items(Long categoryId, String name, Pageable pageable) {
        return itemRepository.findAllOfQueryMethod(categoryId, name, pageable)
                .map(ItemResponse::new);
    }

    @Override
    public ItemResponse item(Long id) {
        return itemRepository.findById(id)
                .map(ItemResponse::new)
                .orElseThrow(() -> new CommonException(ApiResponseCode.ITEM_NOT_FOUND));
    }

    @Override
    @Transactional
    public ItemResponse updateItem(Long id, ItemUpdateRequest itemUpdateRequest) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new CommonException(ApiResponseCode.ITEM_NOT_FOUND));

        Category category = null;

        if (itemUpdateRequest.categoryId() != null) {
            category = categoryRepository.findById(itemUpdateRequest.categoryId())
                    .orElseThrow(() -> new CommonException(ApiResponseCode.CATEGORY_NOT_FOUND));
        }

        item.updateItem(itemUpdateRequest, category);

        return new ItemResponse(item);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new CommonException(ApiResponseCode.ITEM_NOT_FOUND));

        itemRepository.delete(item);
    }
}
