package spring.web.java.application.port.out;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.web.java.domain.Item;

public interface ItemRepositoryPort {

	Item save(Item item);

	Page<Item> findAll(Pageable pageable, String name);

	Optional<Item> findById(Long id);

	void deleteById(Long id);
}
