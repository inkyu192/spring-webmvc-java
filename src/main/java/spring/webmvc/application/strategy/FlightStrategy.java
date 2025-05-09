package spring.webmvc.application.strategy;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.application.dto.command.FlightCreateCommand;
import spring.webmvc.application.dto.command.ProductCreateCommand;
import spring.webmvc.application.dto.result.FlightResult;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.domain.cache.FlightCache;
import spring.webmvc.domain.model.entity.Flight;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.domain.repository.FlightRepository;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlightStrategy implements ProductStrategy {

	private final FlightCache flightCache;
	private final FlightRepository flightRepository;
	private final ObjectMapper objectMapper;

	@Override
	public boolean supports(Category category) {
		return category == Category.FLIGHT;
	}

	@Override
	public ProductResult findByProductId(Long productId) {
		String cache = flightCache.get(productId);

		if (cache != null) {
			try {
				return objectMapper.readValue(cache, FlightResult.class);
			} catch (JsonProcessingException e) {
				log.warn("Failed to deserialize cache for productId={}: {}", productId, e.getMessage());
			}
		}

		FlightResult flightResult = flightRepository.findByProductId(productId)
			.map(FlightResult::new)
			.orElseThrow(() -> new EntityNotFoundException(Flight.class, productId));

		try {
			flightCache.set(productId, objectMapper.writeValueAsString(flightResult));
		} catch (JsonProcessingException e) {
			log.warn("Failed to serialize ticket cache for productId={}: {}", productId, e.getMessage());
		}

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

		return new FlightResult(flight);
	}
}
