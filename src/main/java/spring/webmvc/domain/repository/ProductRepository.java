package spring.webmvc.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import spring.webmvc.application.dto.query.ProductCursorPageQuery;
import spring.webmvc.application.dto.query.ProductOffsetPageQuery;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

public interface ProductRepository {
	Optional<Product> findById(Long id);

	List<Product> findAllById(Iterable<Long> ids);

	CursorPage<Product> findAllWithCursorPage(ProductCursorPageQuery query);

	Page<Product> findAllWithOffsetPage(ProductOffsetPageQuery query);

	Product save(Product product);

	void delete(Product product);
}
