package spring.webmvc.application.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.command.CurationCreateCommand;
import spring.webmvc.application.command.CurationProductCreateCommand;
import spring.webmvc.application.dto.result.CurationResult;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.domain.cache.CacheKey;
import spring.webmvc.domain.cache.ZSetCache;
import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.repository.CurationProductRepository;
import spring.webmvc.domain.repository.CurationRepository;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CurationService {

	private final CurationRepository curationRepository;
	private final CurationProductRepository curationProductRepository;
	private final ProductRepository productRepository;
	private final ZSetCache zSetCache;

	@Transactional
	public CurationResult createCuration(CurationCreateCommand command) {
		Map<Long, Product> productMap = productRepository.findAllById(
				command.products().stream()
					.map(CurationProductCreateCommand::productId)
					.toList()
			)
			.stream()
			.collect(Collectors.toMap(Product::getId, product -> product));

		Curation curation = Curation.create(
			command.title(),
			command.isExposed(),
			command.sortOrder()
		);

		for (CurationProductCreateCommand productCommand : command.products()) {
			Product product = productMap.get(productCommand.productId());

			if (product == null) {
				throw new EntityNotFoundException(Product.class, productCommand.productId());
			}

			curation.addProduct(product, productCommand.sortOrder());
		}

		curationRepository.save(curation);

		return new CurationResult(curation);
	}

	public List<CurationResult> findCurations() {
		String key = CacheKey.CURATION.generate();
		Set<CurationResult> cache = zSetCache.range(key, 0, -1, CurationResult.class);

		if (!ObjectUtils.isEmpty(cache)) {
			return cache.stream().toList();
		}

		List<CurationResult> result = curationRepository.findExposed().stream()
			.map(curation -> {
				CurationResult curationResult = new CurationResult(curation);
				zSetCache.add(key, curationResult, curation.getSortOrder());
				return curationResult;
			})
			.toList();

		zSetCache.expire(key, Duration.ofHours(1));

		return result;
	}

	public Page<ProductResult> findCurationProduct(Pageable pageable, Long id) {
		String key = CacheKey.CURATION_PRODUCT.generate(id);

		long start = pageable.getOffset();
		long end = start + pageable.getPageSize() - 1;

		Long size = zSetCache.size(key);
		long cacheCount = size != null ? size : 0L;
		Set<ProductResult> cacheContent = zSetCache.range(key, start, end, ProductResult.class);

		if (!ObjectUtils.isEmpty(cacheContent)) {
			return new PageImpl<>(cacheContent.stream().toList(), pageable, cacheCount);
		}

		Page<ProductResult> result = curationProductRepository.findAllByCurationId(pageable, id)
			.map(curationProduct -> {
				ProductResult productResult = new ProductResult(curationProduct.getProduct());
				zSetCache.add(key, productResult, curationProduct.getSortOrder());
				return productResult;
			});

		zSetCache.expire(key, Duration.ofHours(1));

		return result;
	}
}
