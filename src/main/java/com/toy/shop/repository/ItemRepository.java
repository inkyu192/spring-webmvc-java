package com.toy.shop.repository;

import com.toy.shop.domain.Item;
import com.toy.shop.repository.custom.ItemCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {

    @Query(
        value =
            "select i" +
            " from Item i" +
            " join fetch i.category c" +
            " where (:categoryId is null or c.id = :categoryId)" +
            " and (:name is null or i.name like concat('%', :name, '%'))",
        countQuery =
            "select count(i)" +
            " from Item i" +
            " join i.category c" +
            " where (:categoryId is null or c.id = :categoryId)" +
            " and (:name is null or i.name like concat('%', :name, '%'))"
    )
    Page<Item> findAllOfQueryMethod(@Param("categoryId") Long categoryId, @Param("name") String name, Pageable pageable);
}
