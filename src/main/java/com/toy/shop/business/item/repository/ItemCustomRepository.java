package com.toy.shop.business.item.repository;

import com.toy.shop.domain.Item;

import java.util.List;

public interface ItemCustomRepository {

    List<Item> findAllOfJpql(Long categoryId, String searchWord);

    List<Item> findAllOfQuery(Long categoryId, String searchWord);
}
