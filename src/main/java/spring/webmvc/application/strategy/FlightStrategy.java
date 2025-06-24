package spring.webmvc.application.strategy;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.application.dto.command.FlightCreateCommand;
import spring.webmvc.application.dto.command.FlightUpdateCommand;
import spring.webmvc.application.dto.command.ProductCreateCommand;
import spring.webmvc.application.dto.command.ProductUpdateCommand;
import spring.webmvc.application.dto.result.FlightResult;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.domain.cache.CacheKey;
import spring.webmvc.domain.cache.ValueCache;
import spring.webmvc.domain.model.entity.Flight;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.domain.repository.FlightRepository;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlightStrategy implements ProductStrategy {

	private final ValueCache valueCache;
	private final FlightRepository flightRepository;

	@Override
	public boolean supports(Category category) {
		return category == Category.FLIGHT;
	}

	@Override
	public ProductResult findByProductId(Long productId) {
		String productKey = CacheKey.PRODUCT.generate(productId);
		FlightResult cache = valueCache.get(productKey, FlightResult.class);

		String viewCountKey = CacheKey.PRODUCT_VIEW_COUNT.generate(productId);
		valueCache.increment(viewCountKey, 1);

		if (cache != null) {
			return cache;
		}

		FlightResult flightResult = flightRepository.findByProductId(productId)
			.map(FlightResult::new)
			.orElseThrow(() -> new EntityNotFoundException(Flight.class, productId));

		valueCache.set(productKey, flightResult, CacheKey.PRODUCT.getTimeout());

		return flightResult;
	}

	@Override
	public ProductResult createProduct(ProductCreateCommand productCreateCommand) {
		FlightCreateCommand flightCreateCommand = (FlightCreateCommand)productCreateCommand;

		Flight flight = flightRepository.save(
			Flight.create(
				flightCreateCommand.getName(),
				flightCreateCommand.getDescription(),
				flightCreateCommand.getPrice(),
				flightCreateCommand.getQuantity(),
				flightCreateCommand.getAirline(),
				flightCreateCommand.getFlightNumber(),
				flightCreateCommand.getDepartureAirport(),
				flightCreateCommand.getArrivalAirport(),
				flightCreateCommand.getDepartureTime(),
				flightCreateCommand.getArrivalTime()
			)
		);

		String key = CacheKey.PRODUCT_STOCK.generate(flight.getProduct().getId());
		valueCache.set(key, flight.getProduct().getQuantity());

		return new FlightResult(flight);
	}

	@Override
	public ProductResult updateProduct(Long productId, ProductUpdateCommand productUpdateCommand) {
		FlightUpdateCommand flightUpdateCommand = (FlightUpdateCommand)productUpdateCommand;

		Flight flight = flightRepository.findByProductId(productId)
			.orElseThrow(() -> new EntityNotFoundException(Flight.class, productId));

		flight.update(
			flightUpdateCommand.getName(),
			flightUpdateCommand.getDescription(),
			flightUpdateCommand.getPrice(),
			flightUpdateCommand.getQuantity(),
			flightUpdateCommand.getAirline(),
			flightUpdateCommand.getFlightNumber(),
			flightUpdateCommand.getDepartureAirport(),
			flightUpdateCommand.getArrivalAirport(),
			flightUpdateCommand.getDepartureTime(),
			flightUpdateCommand.getArrivalTime()
		);

		String key = CacheKey.PRODUCT_STOCK.generate(productId);
		valueCache.set(key, flight.getProduct().getQuantity());

		return new FlightResult(flight);
	}

	@Override
	public void deleteProduct(Long productId) {
		Flight flight = flightRepository.findByProductId(productId)
			.orElseThrow(() -> new EntityNotFoundException(Flight.class, productId));

		String key = CacheKey.PRODUCT_STOCK.generate(productId);
		valueCache.delete(key);

		flightRepository.delete(flight);
	}
}
