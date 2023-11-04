package com.toy.shopwebmvc.repository.custom;

import com.toy.shopwebmvc.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemCustomRepository {

    Page<Item> findAllWithJpql(Pageable pageable, String name);

    Page<Item> findAllWithQuerydsl(Pageable pageable, String name);
}
