package com.toy.shop.repository;

import com.toy.shop.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {

    @Query(
        "select i" +
        " from Item i" +
        " join fetch i.category c" +
        " where (:categoryId is null or c.id = :categoryId)" +
        " and (:name is null or i.name like concat('%', :name, '%'))"
    )
    List<Item> findAllOfQueryMethod(@Param("categoryId") Long categoryId, @Param("name") String name);
}
