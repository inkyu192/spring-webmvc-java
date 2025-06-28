package spring.webmvc.application.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.command.ProductCreateCommand;
import spring.webmvc.application.dto.command.ProductUpdateCommand;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.application.strategy.ProductStrategy;
import spring.webmvc.domain.cache.CacheKey;
import spring.webmvc.domain.cache.ValueCache;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.domain.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

	private final ValueCache valueCache;
	private final ProductRepository productRepository;
	private final Map<Category, ProductStrategy> productStrategyMap;

	public Page<ProductResult> findProducts(Pageable pageable, String name) {
		return productRepository.findAll(pageable, name).map(ProductResult::new);
	}

	public ProductResult findProduct(Long id, Category category) {
		ProductStrategy productStrategy = productStrategyMap.get(category);
		ProductResult productResult = productStrategy.findByProductId(id);

		String key = CacheKey.PRODUCT_VIEW_COUNT.generate(id);
		valueCache.increment(key, 1);

		return productResult;
	}

	@Transactional
	public ProductResult createProduct(ProductCreateCommand command) {
		ProductStrategy productStrategy = productStrategyMap.get(command.getCategory());
		ProductResult productResult = productStrategy.createProduct(command);

		String key = CacheKey.PRODUCT_STOCK.generate(productResult.getId());
		valueCache.set(key, productResult.getQuantity());

		return productResult;
	}

	@Transactional
	public ProductResult updateProduct(Long id, ProductUpdateCommand command) {
		ProductStrategy productStrategy = productStrategyMap.get(command.getCategory());
		ProductResult productResult = productStrategy.updateProduct(id, command);

		String key = CacheKey.PRODUCT_STOCK.generate(id);
		valueCache.set(key, productResult.getQuantity());

		return productResult;
	}

	@Transactional
	public void deleteProduct(Category category, Long id) {
		ProductStrategy productStrategy = productStrategyMap.get(category);
		productStrategy.deleteProduct(id);

		String key = CacheKey.PRODUCT_STOCK.generate(id);
		valueCache.delete(key);
	}
}
