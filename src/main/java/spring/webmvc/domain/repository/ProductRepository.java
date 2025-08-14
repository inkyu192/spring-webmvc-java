package spring.webmvc.domain.repository;

import java.util.List;
import java.util.Optional;

import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

public interface ProductRepository {
	Optional<Product> findById(Long id);

	CursorPage<Product> findAll(Long nextCursorId, int size, String name);

	List<Product> findAllById(Iterable<Long> ids);
}