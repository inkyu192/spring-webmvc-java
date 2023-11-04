package com.toy.shopwebmvc.repository;

import com.toy.shopwebmvc.domain.Order;
import com.toy.shopwebmvc.repository.custom.OrderCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository {
}
