package com.toy.shopwebmvc.service;

import com.toy.shopwebmvc.dto.request.ItemSaveRequest;
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

        return new ItemResponse(item);
    }

    public Page<ItemResponse> findItems(Pageable pageable, String name) {
        return itemRepository.findAllWithQuerydsl(pageable, name)
                .map(ItemResponse::new);
    }

    public ItemResponse findItem(Long id) {
        return itemRepository.findById(id)
                .map(ItemResponse::new)
                .orElseThrow(() -> new CommonException(DATA_NOT_FOUND));
    }

    @Transactional
    public ItemResponse updateItem(Long id, ItemSaveRequest itemSaveRequest) {
        Item item = itemRepository.findById(id)
                .map(findItem -> {
                    findItem.update(
                            itemSaveRequest.name(),
                            itemSaveRequest.description(),
                            itemSaveRequest.price(),
                            itemSaveRequest.quantity(),
                            itemSaveRequest.category()
                    );

                    return findItem;
                })
                .orElseGet(() -> Item.create(
                        itemSaveRequest.name(),
                        itemSaveRequest.description(),
                        itemSaveRequest.price(),
                        itemSaveRequest.quantity(),
                        itemSaveRequest.category()
                ));

        return new ItemResponse(item);
    }

    @Transactional
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
