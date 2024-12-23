package spring.web.java.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.web.java.domain.Item;

public interface ItemJpaCustomRepositoryPort {

	Page<Item> findAll(Pageable pageable, String name);
}
