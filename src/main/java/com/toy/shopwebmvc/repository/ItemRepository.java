package com.toy.shopwebmvc.repository;

import com.toy.shopwebmvc.domain.Item;
import com.toy.shopwebmvc.repository.custom.ItemCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {
}
