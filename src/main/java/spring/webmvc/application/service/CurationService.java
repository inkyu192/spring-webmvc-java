package spring.webmvc.application.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.command.CurationCreateCommand;
import spring.webmvc.application.dto.command.CurationProductCreateCommand;
import spring.webmvc.application.dto.result.CurationCursorPageResult;
import spring.webmvc.application.dto.result.CurationDetailResult;
import spring.webmvc.application.dto.result.CurationOffsetPageResult;
import spring.webmvc.application.dto.result.CurationSummaryResult;
import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.entity.CurationProduct;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.entity.UserCurationProduct;
import spring.webmvc.domain.model.enums.CurationCategory;
import spring.webmvc.domain.repository.CurationProductRepository;
import spring.webmvc.domain.repository.CurationRepository;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.domain.repository.UserCurationProductRepository;
import spring.webmvc.infrastructure.exception.NotFoundEntityException;
import spring.webmvc.infrastructure.persistence.dto.CursorPage;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CurationService {
	private final CurationRepository curationRepository;
	private final CurationProductRepository curationProductRepository;
	private final ProductRepository productRepository;
	private final UserCurationProductRepository userCurationProductRepository;

	@Transactional
	public CurationDetailResult createCuration(CurationCreateCommand command) {
		List<Long> requestProductIds = command.products().stream()
			.map(CurationProductCreateCommand::productId)
			.toList();

		Map<Long, Product> productMap = productRepository.findAllById(requestProductIds).stream()
			.collect(Collectors.toMap(Product::getId, Function.identity()));

		Curation curation = Curation.create(
			command.title(),
			command.category(),
			command.isExposed(),
			command.sortOrder()
		);

		command.products().forEach(productCommand -> {
			Product product = productMap.get(productCommand.productId());
			if (product != null) {
				curation.addProduct(product, productCommand.sortOrder());
			}
		});

		curationRepository.save(curation);

		return CurationDetailResult.of(curation);
	}

	@Cacheable(value = "curations", key = "'curation:category:' + #category")
	public List<CurationSummaryResult> findCurationsCached(CurationCategory category) {
		return findCurations(category);
	}

	public List<CurationSummaryResult> findCurations(CurationCategory category) {
		return curationRepository.findAllByCategory(category).stream()
			.map(CurationSummaryResult::of)
			.toList();
	}

	public CurationOffsetPageResult findCurationProductWithOffsetPage(Long id, Pageable pageable) {
		Curation curation = curationRepository.findById(id)
			.orElseThrow(() -> new NotFoundEntityException(Curation.class, id));

		Page<CurationProduct> page = curationProductRepository.findAllWithOffsetPage(id, pageable);

		return CurationOffsetPageResult.of(curation, page);
	}

	@Cacheable(
		value = "curationProducts",
		key = "'curation:' + #curationId + ':user:' + #userId + ':cursor:' + #cursorId"
	)
	public CurationCursorPageResult findCurationProductCached(Long userId, Long curationId, Long cursorId) {
		if (userId == null) {
			return findCurationProductWithCursorPage(curationId, cursorId);
		}

		CurationCursorPageResult userResult = findUserCurationProduct(userId, curationId);

		return userResult != null ? userResult : findCurationProductWithCursorPage(curationId, cursorId);
	}

	public CurationCursorPageResult findCurationProductWithCursorPage(Long curationId, Long cursorId) {
		Curation curation = curationRepository.findById(curationId)
			.orElseThrow(() -> new NotFoundEntityException(Curation.class, curationId));

		CursorPage<CurationProduct> page = curationProductRepository.findAllWithCursorPage(curationId, cursorId);

		return CurationCursorPageResult.of(curation, page);
	}

	public CurationCursorPageResult findUserCurationProduct(Long userId, Long curationId) {
		UserCurationProduct userCurationProduct =
			userCurationProductRepository.findByUserIdAndCurationId(userId, curationId);

		if (userCurationProduct == null) {
			return null;
		}

		Curation curation = curationRepository.findById(curationId)
			.orElseThrow(() -> new NotFoundEntityException(Curation.class, curationId));

		List<Long> productIds = userCurationProduct.getProductIds();

		Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
			.collect(Collectors.toMap(Product::getId, Function.identity()));

		List<Product> products = productIds.stream()
			.map(productMap::get)
			.filter(Objects::nonNull)
			.toList();

		return CurationCursorPageResult.of(curation, products);
	}
}
