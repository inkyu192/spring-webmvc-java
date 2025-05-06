package spring.webmvc.application.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.application.strategy.ProductStrategy;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.domain.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final List<ProductStrategy> productStrategies;

	public Page<ProductResult> findProducts(Pageable pageable, String name) {
		return productRepository.findAll(pageable, name).map(ProductResult::new);
	}

	public ProductResult findProduct(Long id, Category category) {
		ProductStrategy productStrategy = productStrategies.stream()
			.filter(it -> it.supports(category))
			.findFirst()
			.orElseThrow(IllegalStateException::new);

		return productStrategy.findByProductId(id);
	}
}
