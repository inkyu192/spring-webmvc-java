package com.toy.shopwebmvc.repository.custom;

import com.toy.shopwebmvc.domain.Item;

import java.util.List;

public interface ItemCustomRepository {

    List<Item> findAllWithJpql(String name);

    List<Item> findAllWithQuerydsl(String name);
}
