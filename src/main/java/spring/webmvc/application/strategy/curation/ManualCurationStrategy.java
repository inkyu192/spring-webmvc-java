package spring.webmvc.application.strategy.curation;

import java.util.List;
import java.util.Map;
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
import spring.webmvc.domain.model.entity.UserProductBadge;
import spring.webmvc.domain.model.enums.CurationType;
import spring.webmvc.domain.repository.CurationProductRepository;
import spring.webmvc.domain.repository.UserProductBadgeRepository;

@Component
@RequiredArgsConstructor
public class ManualCurationStrategy implements CurationProductStrategy {

	private final CurationProductRepository curationProductRepository;
	private final UserProductBadgeRepository userProductBadgeRepository;

	@Override
	public CurationType getType() {
		return CurationType.MANUAL;
	}

	@Override
	public CursorPage<CurationProductResult> findProductsWithCursorPage(Curation curation, Long cursorId, Long userId) {
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

	@Override
	public Page<CurationProductResult> findProductsWithOffsetPage(Curation curation, Pageable pageable) {
		Page<CurationProduct> page = curationProductRepository.findAllWithOffsetPage(curation.getId(), pageable);
		return page.map(CurationProductResult::of);
	}
}
