package spring.webmvc.application.strategy.curation;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.result.CurationProductResult;
import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.entity.CurationProduct;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.entity.UserCurationProduct;
import spring.webmvc.domain.model.entity.UserProductBadge;
import spring.webmvc.domain.model.enums.CurationType;
import spring.webmvc.domain.repository.CurationProductRepository;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.domain.repository.UserCurationProductRepository;
import spring.webmvc.domain.repository.UserProductBadgeRepository;

@Component
@RequiredArgsConstructor
public class PersonalizedCurationStrategy implements CurationProductStrategy {

	private final UserCurationProductRepository userCurationProductRepository;
	private final ProductRepository productRepository;
	private final UserProductBadgeRepository userProductBadgeRepository;
	private final CurationProductRepository curationProductRepository;

	@Override
	public CurationType getType() {
		return CurationType.PERSONALIZED;
	}

	@Override
	public CursorPage<CurationProductResult> findProductsWithCursorPage(Curation curation, Long cursorId, Long userId) {
		if (userId == null) {
			return fallbackToManual(curation, cursorId, null);
		}

		UserCurationProduct userCurationProduct =
			userCurationProductRepository.findByUserIdAndCurationId(userId, curation.getId());

		if (userCurationProduct == null) {
			return fallbackToManual(curation, cursorId, userId);
		}

		List<Long> productIds = userCurationProduct.getProductIds();
		Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
			.collect(Collectors.toMap(Product::getId, Function.identity()));

		List<Product> products = productIds.stream()
			.map(productMap::get)
			.filter(Objects::nonNull)
			.toList();

		Map<Long, UserProductBadge> badgeMap = userProductBadgeRepository
			.findByUserIdAndProductIds(userId, productIds).stream()
			.collect(Collectors.toMap(
				b -> Long.parseLong(b.getSk().replace("PRODUCT#", "")),
				Function.identity()
			));

		List<CurationProductResult> results = products.stream()
			.map(p -> CurationProductResult.of(p, badgeMap.get(p.getId())))
			.toList();

		return new CursorPage<>(results, (long)results.size(), false, null);
	}

	@Override
	public Page<CurationProductResult> findProductsWithOffsetPage(Curation curation, Pageable pageable) {
		Page<CurationProduct> page = curationProductRepository.findAllWithOffsetPage(curation.getId(), pageable);
		return page.map(CurationProductResult::of);
	}

	private CursorPage<CurationProductResult> fallbackToManual(Curation curation, Long cursorId, Long userId) {
		CursorPage<CurationProduct> page = curationProductRepository.findAllWithCursorPage(curation.getId(), cursorId);

		if (userId != null) {
			List<Long> productIds = page.content().stream()
				.map(cp -> cp.getProduct().getId())
				.toList();
			Map<Long, UserProductBadge> badgeMap = userProductBadgeRepository
				.findByUserIdAndProductIds(userId, productIds).stream()
				.collect(Collectors.toMap(
					b -> Long.parseLong(b.getSk().replace("PRODUCT#", "")),
					Function.identity()
				));
			return page.map(cp -> CurationProductResult.of(cp, badgeMap.get(cp.getProduct().getId())));
		}

		return page.map(CurationProductResult::of);
	}
}
