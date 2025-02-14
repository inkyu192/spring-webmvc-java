package spring.web.java.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.web.java.domain.model.entity.Item;

public interface ItemJpaRepository extends JpaRepository<Item, Long> {
}
