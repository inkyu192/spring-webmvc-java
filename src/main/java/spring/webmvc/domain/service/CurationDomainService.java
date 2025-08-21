package spring.webmvc.domain.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import spring.webmvc.domain.model.cache.CurationCache;
import spring.webmvc.domain.model.cache.CurationProductCache;
import spring.webmvc.domain.model.cache.ProductCache;
import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.entity.CurationProduct;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
public class CurationDomainService {

	public Curation createCuration(
		String title,
		boolean isExposed,
		Long sortOrder,
		List<Long> requestProductIds,
		List<Product> products
	) {
		validateProducts(requestProductIds, products);

		Curation curation = Curation.create(title, isExposed, sortOrder);

		addProduct(curation, products, requestProductIds);

		return curation;
	}

	private void validateProducts(List<Long> requestProductIds, List<Product> products) {
		Set<Long> existingProductIds = products.stream()
			.map(Product::getId)
			.filter(Objects::nonNull)
			.collect(Collectors.toSet());

		List<Long> missingProductIds = requestProductIds.stream()
			.filter(id -> !existingProductIds.contains(id))
			.toList();

		if (!missingProductIds.isEmpty()) {
			throw new EntityNotFoundException(Product.class, missingProductIds.getFirst());
		}
	}

	private void addProduct(Curation curation, List<Product> products, List<Long> requestProductIds) {
		requestProductIds.forEach(requestProductId ->
			products.stream()
				.filter(it -> it.getId().equals(requestProductId))
				.findFirst()
				.ifPresent(product -> curation.addProduct(CurationProduct.create(curation, product)))
		);
	}

	public CurationCache createCurationCache(Curation curation) {
		return CurationCache.create(
			curation.getId(),
			curation.getTitle()
		);
	}

	public CurationProductCache createCurationProductCache(Curation curation, CursorPage<Product> productPage) {
		CurationCache curationCache = createCurationCache(curation);
		CursorPage<ProductCache> productCachePage = productPage.map(product ->
			ProductCache.create(
				product.getId(),
				product.getCategory(),
				product.getName(),
				product.getDescription(),
				product.getPrice(),
				product.getQuantity(),
				product.getCreatedAt()
			)
		);

		return new CurationProductCache(curationCache, productCachePage);
	}
}
