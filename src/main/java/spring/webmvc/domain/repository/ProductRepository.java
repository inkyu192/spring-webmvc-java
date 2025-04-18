package spring.webmvc.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import spring.webmvc.domain.model.entity.Product;

public interface ProductRepository {

	Page<Product> findAll(Pageable pageable, String name);

	Optional<Product> findById(Long id);

	List<Product> findAllById(Iterable<Long> ids);

	Product save(Product product);

	void delete(Product product);
}