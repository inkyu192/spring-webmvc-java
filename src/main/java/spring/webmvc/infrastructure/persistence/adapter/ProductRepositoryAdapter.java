package spring.webmvc.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.query.ProductCursorPageQuery;
import spring.webmvc.application.dto.query.ProductOffsetPageQuery;
import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.infrastructure.persistence.jpa.ProductJpaRepository;
import spring.webmvc.infrastructure.persistence.jpa.ProductQuerydslRepository;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

	private final ProductJpaRepository jpaRepository;
	private final ProductQuerydslRepository querydslRepository;

	@Override
	public Optional<Product> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public List<Product> findAllById(Iterable<Long> ids) {
		return jpaRepository.findAllById(ids);
	}

	@Override
	public CursorPage<Product> findAllWithCursorPage(ProductCursorPageQuery query) {
		return querydslRepository.findAllWithCursorPage(query);
	}

	@Override
	public Page<Product> findAllWithOffsetPage(ProductOffsetPageQuery query) {
		return querydslRepository.findAllWithOffsetPage(query);
	}

	@Override
	public Product save(Product product) {
		return jpaRepository.save(product);
	}

	@Override
	public void delete(Product product) {
		jpaRepository.delete(product);
	}
}
