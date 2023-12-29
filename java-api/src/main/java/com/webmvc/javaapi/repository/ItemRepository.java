package com.webmvc.javaapi.repository;


import com.webmvc.javaapi.domain.Item;
import com.webmvc.javaapi.repository.custom.ItemCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {
}
