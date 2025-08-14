package spring.webmvc.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;
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
	public CursorPage<Product> findAll(Long nextCursorId, int size, String name) {
		return querydslRepository.findAll(nextCursorId, size, name);
	}

	@Override
	public List<Product> findAllById(Iterable<Long> ids) {
		return jpaRepository.findAllById(ids);
	}
}