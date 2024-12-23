package spring.web.java.application.port.out;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.web.java.domain.Item;

public interface ItemJpaRepositoryPort extends JpaRepository<Item, Long> {
}
