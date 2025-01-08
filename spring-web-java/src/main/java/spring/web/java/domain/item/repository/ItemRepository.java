package spring.web.java.domain.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.web.java.domain.item.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {
}
