package com.webmvc.javaapi.repository.custom;

import com.webmvc.javaapi.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemCustomRepository {

    Page<Item> findAllWithJpql(Pageable pageable, String name);

    Page<Item> findAllWithQuerydsl(Pageable pageable, String name);
}
