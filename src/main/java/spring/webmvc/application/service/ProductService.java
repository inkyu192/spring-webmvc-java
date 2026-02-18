package spring.webmvc.application.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring.webmvc.application.dto.command.ProductCreateCommand;
import spring.webmvc.application.dto.command.ProductUpdateCommand;
import spring.webmvc.application.dto.query.ProductCursorPageQuery;
import spring.webmvc.application.dto.query.ProductOffsetPageQuery;
import spring.webmvc.application.dto.result.ProductAttributeResult;
import spring.webmvc.application.dto.result.ProductDetailResult;
import spring.webmvc.application.dto.result.ProductSummaryResult;
import spring.webmvc.application.event.ProductViewEvent;
import spring.webmvc.application.strategy.product.ProductAttributeStrategy;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.ProductCategory;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.infrastructure.exception.NotFoundEntityException;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

@Service
@Transactional(readOnly = true)
public class ProductService {

	private final ProductRepository productRepository;
	private final ApplicationEventPublisher eventPublisher;
	private final Map<ProductCategory, ProductAttributeStrategy> productAttributeStrategyMap;

	public ProductService(
		ProductRepository productRepository,
		ApplicationEventPublisher eventPublisher,
		List<ProductAttributeStrategy> productStrategies
	) {
		this.productRepository = productRepository;
		this.eventPublisher = eventPublisher;

		Set<ProductCategory> duplicates = productStrategies.stream()
			.collect(Collectors.groupingBy(ProductAttributeStrategy::getCategory))
			.entrySet()
			.stream()
			.filter(entry -> entry.getValue().size() > 1)
			.map(Map.Entry::getKey)
			.collect(Collectors.toSet());

		if (!duplicates.isEmpty()) {
			throw new IllegalStateException("중복된 ProductAttributeStrategy가 존재합니다: %s".formatted(duplicates));
		}

		this.productAttributeStrategyMap = productStrategies.stream()
			.collect(Collectors.toMap(ProductAttributeStrategy::getCategory, Function.identity()));
	}

	public CursorPage<ProductSummaryResult> findProductsWithCursorPage(ProductCursorPageQuery query) {
		return productRepository.findAllWithCursorPage(query)
			.map(ProductSummaryResult::of);
	}

	public Page<ProductSummaryResult> findProductsWithOffsetPage(ProductOffsetPageQuery query) {
		return productRepository.findAllWithOffsetPage(query)
			.map(ProductSummaryResult::of);
	}

	@Cacheable(value = "product", key = "'product:' + #id")
	public ProductDetailResult findProductCached(Long id) {
		return findProduct(id);
	}

	public ProductDetailResult findProduct(Long id) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new NotFoundEntityException(Product.class, id));

		ProductAttributeStrategy strategy = productAttributeStrategyMap.get(product.getCategory());
		if (strategy == null) {
			throw new IllegalStateException("구현되지 않은 상품 카테고리입니다: %s".formatted(product.getCategory()));
		}

		ProductAttributeResult attributeResult = strategy.findByProductId(id);

		return ProductDetailResult.of(product, attributeResult);
	}

	public void incrementProductViewCount(Long id) {
		eventPublisher.publishEvent(new ProductViewEvent(id));
	}

	@Transactional
	public ProductDetailResult createProduct(ProductCreateCommand command) {
		Product product = Product.create(
			command.category(),
			command.name(),
			command.description(),
			command.price(),
			command.quantity(),
			command.exposureAttribute()
		);

		productRepository.save(product);

		ProductAttributeStrategy strategy = productAttributeStrategyMap.get(product.getCategory());
		if (strategy == null) {
			throw new IllegalStateException("구현되지 않은 상품 카테고리입니다: %s".formatted(product.getCategory()));
		}

		ProductAttributeResult attributeResult = strategy.create(product, command.attribute());

		return ProductDetailResult.of(product, attributeResult);
	}

	@Transactional
	@Caching(evict = {
		@CacheEvict(value = "product", key = "'product:' + #command.id"),
		@CacheEvict(value = "productStock", key = "'product:' + #command.id + ':stock'")
	})
	public ProductDetailResult updateProduct(ProductUpdateCommand command) {
		Product product = productRepository.findById(command.id())
			.orElseThrow(() -> new NotFoundEntityException(Product.class, command.id()));

		product.update(
			command.status(),
			command.name(),
			command.description(),
			command.price(),
			command.quantity(),
			command.exposureAttribute()
		);

		ProductAttributeStrategy strategy = productAttributeStrategyMap.get(product.getCategory());
		if (strategy == null) {
			throw new IllegalStateException("구현되지 않은 상품 카테고리입니다: %s".formatted(product.getCategory()));
		}

		ProductAttributeResult attributeResult = strategy.update(command.id(), command.attribute());

		return ProductDetailResult.of(product, attributeResult);
	}

	@Transactional
	@Caching(evict = {
		@CacheEvict(value = "product", key = "'product:' + #id"),
		@CacheEvict(value = "productStock", key = "'product:' + #id + ':stock'")
	})
	public void deleteProduct(Long id) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new NotFoundEntityException(Product.class, id));

		ProductAttributeStrategy strategy = productAttributeStrategyMap.get(product.getCategory());
		if (strategy == null) {
			throw new IllegalStateException("구현되지 않은 상품 카테고리입니다: %s".formatted(product.getCategory()));
		}

		strategy.deleteProduct(id);
		productRepository.delete(product);
	}
}
