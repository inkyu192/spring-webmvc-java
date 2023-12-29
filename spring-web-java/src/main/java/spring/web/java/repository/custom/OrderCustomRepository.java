package spring.web.java.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.web.java.constant.DeliveryStatus;
import spring.web.java.constant.OrderStatus;
import spring.web.java.domain.Order;

public interface OrderCustomRepository {

    Page<Order> findAllWithJpql(
            Pageable pageable,
            Long memberId,
            OrderStatus orderStatus,
            DeliveryStatus deliveryStatus
    );

    Page<Order> findAllWithQuerydsl(
            Pageable pageable,
            Long memberId,
            OrderStatus orderStatus,
            DeliveryStatus deliveryStatus
    );
}
