package spring.webmvc.application.strategy.curation;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.query.ProductCursorPageQuery;
import spring.webmvc.application.dto.query.ProductOffsetPageQuery;
import spring.webmvc.application.dto.result.CurationProductResult;
import spring.webmvc.domain.dto.CursorPage;
import spring.webmvc.domain.model.entity.Curation;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.entity.UserProductBadge;
import spring.webmvc.domain.model.enums.CurationType;
import spring.webmvc.domain.model.enums.ProductStatus;
import spring.webmvc.domain.repository.ProductRepository;
import spring.webmvc.domain.repository.UserProductBadgeRepository;

@Component
@RequiredArgsConstructor
public class SearchCurationStrategy implements CurationProductStrategy {

	private final ProductRepository productRepository;
	private final UserProductBadgeRepository userProductBadgeRepository;

	@Override
	public CurationType getType() {
		return CurationType.SEARCH;
	}

	@Override
	public CursorPage<CurationProductResult> findProductsWithCursorPage(Curation curation, Long cursorId, Long userId) {
		List<Long> tagIds = curation.getAttribute() != null
			? curation.getAttribute().tagIds()
			: List.of();

		if (tagIds.isEmpty()) {
			return CursorPage.empty();
		}

		ProductCursorPageQuery query = new ProductCursorPageQuery(
			cursorId,
			null,
			ProductStatus.SELLING,
			tagIds
		);

		CursorPage<Product> page = productRepository.findAllWithCursorPage(query);

		if (userId != null) {
			List<Long> productIds = page.content().stream()
				.map(Product::getId)
				.toList();
			Map<Long, UserProductBadge> badgeMap = userProductBadgeRepository
				.findByUserIdAndProductIds(userId, productIds).stream()
				.collect(Collectors.toMap(
					b -> Long.parseLong(b.getSk().replace("PRODUCT#", "")),
					Function.identity()
				));
			return page.map(p -> CurationProductResult.of(p, badgeMap.get(p.getId())));
		}

		return page.map(CurationProductResult::of);
	}

	@Override
	public Page<CurationProductResult> findProductsWithOffsetPage(Curation curation, Pageable pageable) {
		List<Long> tagIds = curation.getAttribute() != null
			? curation.getAttribute().tagIds()
			: List.of();

		if (tagIds.isEmpty()) {
			return Page.empty(pageable);
		}

		ProductOffsetPageQuery query = new ProductOffsetPageQuery(
			pageable,
			null,
			ProductStatus.SELLING,
			tagIds
		);

		Page<Product> page = productRepository.findAllWithOffsetPage(query);

		return page.map(CurationProductResult::of);
	}
}
