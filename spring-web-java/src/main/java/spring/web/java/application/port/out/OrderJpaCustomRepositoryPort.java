package spring.web.java.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.web.java.common.constant.DeliveryStatus;
import spring.web.java.common.constant.OrderStatus;
import spring.web.java.domain.Order;

public interface OrderJpaCustomRepositoryPort {

	Page<Order> findAll(Pageable pageable, Long memberId, OrderStatus orderStatus, DeliveryStatus deliveryStatus);
}
