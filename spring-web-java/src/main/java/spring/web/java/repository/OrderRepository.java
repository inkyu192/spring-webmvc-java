package spring.web.java.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import spring.web.java.domain.Order;
import spring.web.java.repository.custom.OrderCustomRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository {
}
