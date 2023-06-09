package com.toy.shop.service;

import com.toy.shop.common.ResultCode;
import com.toy.shop.domain.Category;
import com.toy.shop.domain.Item;
import com.toy.shop.dto.ItemDto;
import com.toy.shop.exception.CommonException;
import com.toy.shop.repository.CategoryRepository;
import com.toy.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ItemDto.Response saveItem(ItemDto.Save requestDto) {
        Category category = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(() -> new CommonException(ResultCode.CATEGORY_NOT_FOUND));

        Item item = Item.createItem(requestDto, category);

        itemRepository.save(item);

        return new ItemDto.Response(item);
    }

    @Override
    public List<ItemDto.Response> items(Long categoryId, String name) {
        List<Item> items = itemRepository.findAllOfQueryMethod(categoryId, name);

        return items.stream()
                .map(ItemDto.Response::new)
                .toList();
    }

    @Override
    public ItemDto.Response item(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new CommonException(ResultCode.ITEM_NOT_FOUND));

        return new ItemDto.Response(item);
    }

    @Override
    @Transactional
    public ItemDto.Response updateItem(Long id, ItemDto.Update requestDto) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new CommonException(ResultCode.ITEM_NOT_FOUND));

        Category category = null;

        if (requestDto.getCategoryId() != null) {
            category = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(() -> new CommonException(ResultCode.CATEGORY_NOT_FOUND));
        }

        item.updateItem(requestDto, category);

        return new ItemDto.Response(item);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new CommonException(ResultCode.ITEM_NOT_FOUND));

        itemRepository.delete(item);
    }
}
