package spring.webmvc.application.strategy;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.result.FlightResult;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.domain.cache.FlightCache;
import spring.webmvc.domain.model.entity.Flight;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.domain.repository.FlightRepository;
import spring.webmvc.infrastructure.common.JsonSupport;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Component
@RequiredArgsConstructor
public class FlightStrategy implements ProductStrategy {

	private final FlightCache flightCache;
	private final FlightRepository flightRepository;
	private final JsonSupport jsonSupport;

	@Override
	public boolean supports(Category category) {
		return category == Category.FLIGHT;
	}

	@Override
	public ProductResult findByProductId(Long productId) {
		String cache = flightCache.get(productId);

		if (cache != null) {
			return jsonSupport.readValue(cache, FlightResult.class);
		}

		FlightResult flightResult = flightRepository.findByProductId(productId)
			.map(FlightResult::new)
			.orElseThrow(() -> new EntityNotFoundException(Flight.class, productId));

		flightCache.set(productId, jsonSupport.writeValueAsString(flightResult));

		return flightResult;
	}
}
