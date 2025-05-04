package spring.webmvc.application.strategy;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.result.AccommodationResult;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.domain.cache.AccommodationCache;
import spring.webmvc.domain.model.entity.Accommodation;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.domain.repository.AccommodationRepository;
import spring.webmvc.infrastructure.common.JsonSupport;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Component
@RequiredArgsConstructor
public class AccommodationStrategy implements ProductStrategy {

	private final AccommodationCache accommodationCache;
	private final AccommodationRepository accommodationRepository;
	private final JsonSupport jsonSupport;

	@Override
	public boolean supports(Category category) {
		return category == Category.ACCOMMODATION;
	}

	@Override
	public ProductResult findByProductId(Long productId) {
		String cache = accommodationCache.get(productId);

		if (cache != null) {
			return jsonSupport.readValue(cache, AccommodationResult.class);
		}

		AccommodationResult accommodationResult = accommodationRepository.findByProductId(productId)
			.map(AccommodationResult::new)
			.orElseThrow(() -> new EntityNotFoundException(Accommodation.class, productId));

		accommodationCache.set(productId, jsonSupport.writeValueAsString(accommodationResult));

		return accommodationResult;
	}
}
