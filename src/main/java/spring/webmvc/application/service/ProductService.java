package spring.webmvc.application.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.command.ProductCreateCommand;
import spring.webmvc.application.dto.command.ProductUpdateCommand;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.application.strategy.ProductStrategy;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.presentation.exception.EntityNotFoundException;
import spring.webmvc.presentation.exception.StrategyNotImplementedException;

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
		ProductStrategy productStrategy = getStrategy(category);

		return productStrategy.findByProductId(id);
	}

	@Transactional
	public ProductResult createProduct(ProductCreateCommand command) {
		ProductStrategy productStrategy = getStrategy(command.getCategory());

		return productStrategy.createProduct(command);
	}

	@Transactional
	public ProductResult updateProduct(Long productId, ProductUpdateCommand command) {
		productRepository.findById(productId)
			.orElseThrow(() -> new EntityNotFoundException(Product.class, productId));

		ProductStrategy productStrategy = getStrategy(command.getCategory());

		return productStrategy.updateProduct(productId, command);
	}

	private ProductStrategy getStrategy(Category category) {
		return productStrategies.stream()
			.filter(it -> it.supports(category))
			.findFirst()
			.orElseThrow(() -> new StrategyNotImplementedException(ProductStrategy.class, category));
	}
}
