package com.toy.shop.service;

import com.toy.shop.dto.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {

    ItemDto.Response saveItem(ItemDto.Save requestDto);

    Page<ItemDto.Response> items(Long categoryId, String name, Pageable pageable);

    ItemDto.Response item(Long id);

    ItemDto.Response updateItem(Long id, ItemDto.Update requestDto);

    void deleteItem(Long id);
}
