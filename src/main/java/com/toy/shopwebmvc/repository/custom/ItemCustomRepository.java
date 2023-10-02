package com.toy.shopwebmvc.repository.custom;

import com.toy.shopwebmvc.domain.Item;

import java.util.List;

public interface ItemCustomRepository {

    List<Item> findAllOfJpql(String searchWord);

    List<Item> findAllOfQuery(String searchWord);
}
