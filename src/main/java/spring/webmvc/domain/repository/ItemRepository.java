package spring.webmvc.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.webmvc.domain.model.entity.Item;

public interface ItemRepository {

	Page<Item> findAll(Pageable pageable, String name);

	Optional<Item> findById(Long id);

	List<Item> findAllById(Iterable<Long> ids);

	Item save(Item item);

	void delete(Item item);
}
