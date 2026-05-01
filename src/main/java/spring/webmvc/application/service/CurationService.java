package spring.webmvc.application.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring.webmvc.application.dto.command.CurationCreateCommand;
import spring.webmvc.application.dto.command.CurationProductCreateCommand;
import spring.webmvc.application.dto.result.CurationCursorPageResult;
import spring.webmvc.application.dto.result.CurationDetailResult;
import spring.webmvc.application.dto.result.CurationOffsetPageResult;
import spring.webmvc.application.dto.result.CurationProductResult;
import spring.webmvc.application.dto.result.CurationSummaryResult;
import spring.webmvc.application.strategy.curation.CurationProductStrategy;
import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.CurationPlacement;
import spring.webmvc.domain.model.enums.CurationType;
import spring.webmvc.domain.repository.CurationRepository;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.infrastructure.exception.NotFoundEntityException;

@Service
@Transactional(readOnly = true)
public class CurationService {

	private final CurationRepository curationRepository;
	private final ProductRepository productRepository;
	private final Map<CurationType, CurationProductStrategy> strategyMap;

	public CurationService(
		CurationRepository curationRepository,
		ProductRepository productRepository,
		List<CurationProductStrategy> strategies
	) {
		this.curationRepository = curationRepository;
		this.productRepository = productRepository;
		this.strategyMap = strategies.stream()
			.collect(Collectors.toMap(CurationProductStrategy::getType, Function.identity()));
	}

	@Transactional
	public CurationDetailResult createCuration(CurationCreateCommand command) {
		List<Long> requestProductIds = command.products().stream()
			.map(CurationProductCreateCommand::productId)
			.toList();

		Map<Long, Product> productMap = productRepository.findAllById(requestProductIds).stream()
			.collect(Collectors.toMap(Product::getId, Function.identity()));

		Curation curation = Curation.create(
			command.title(),
			command.placement(),
			command.type(),
			command.attribute(),
			command.exposureAttribute(),
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

	@Cacheable(value = "curations", key = "'curation:placement:' + #placement")
	public List<CurationSummaryResult> findCurationsCached(CurationPlacement placement) {
		return findCurations(placement);
	}

	public List<CurationSummaryResult> findCurations(CurationPlacement placement) {
		return curationRepository.findAllByPlacement(placement).stream()
			.map(CurationSummaryResult::of)
			.toList();
	}

	public CurationOffsetPageResult findCurationProductWithOffsetPage(Long id, Pageable pageable) {
		Curation curation = curationRepository.findById(id)
			.orElseThrow(() -> new NotFoundEntityException(Curation.class, id));

		CurationProductStrategy strategy = getStrategy(curation.getType());
		var productPage = strategy.findProductsWithOffsetPage(curation, pageable);

		return CurationOffsetPageResult.of(curation, productPage);
	}

	@Cacheable(
		value = "curationProducts",
		key = "'curation:' + #curationId + ':user:' + #userId + ':cursor:' + #cursorId"
	)
	public CurationCursorPageResult findCurationProductCached(Long userId, Long curationId, Long cursorId) {
		return findCurationProductWithCursorPage(curationId, cursorId, userId);
	}

	public CurationCursorPageResult findCurationProductWithCursorPage(Long curationId, Long cursorId, Long userId) {
		Curation curation = curationRepository.findById(curationId)
			.orElseThrow(() -> new NotFoundEntityException(Curation.class, curationId));

		CurationProductStrategy strategy = getStrategy(curation.getType());
		CursorPage<CurationProductResult> productPage = strategy.findProductsWithCursorPage(curation, cursorId, userId);

		return CurationCursorPageResult.of(curation, productPage);
	}

	private CurationProductStrategy getStrategy(CurationType type) {
		CurationType effectiveType = type != null ? type : CurationType.MANUAL;
		return strategyMap.get(effectiveType);
	}
}
