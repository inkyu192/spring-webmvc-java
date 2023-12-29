package spring.web.java.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import spring.web.java.domain.Item;
import spring.web.java.repository.custom.ItemCustomRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {
}
