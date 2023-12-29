package com.webmvc.javaapi.repository;


import com.webmvc.javaapi.domain.Order;
import com.webmvc.javaapi.repository.custom.OrderCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository {
}
