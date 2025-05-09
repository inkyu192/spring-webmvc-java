package spring.webmvc.application.strategy;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.application.dto.command.AccommodationCreateCommand;
import spring.webmvc.application.dto.command.ProductCreateCommand;
import spring.webmvc.application.dto.result.AccommodationResult;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.domain.cache.AccommodationCache;
import spring.webmvc.domain.model.entity.Accommodation;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.domain.repository.AccommodationRepository;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccommodationStrategy implements ProductStrategy {

	private final AccommodationCache accommodationCache;
	private final AccommodationRepository accommodationRepository;
	private final ObjectMapper objectMapper;

	@Override
	public boolean supports(Category category) {
		return category == Category.ACCOMMODATION;
	}

	@Override
	public ProductResult findByProductId(Long productId) {
		String cache = accommodationCache.get(productId);

		if (cache != null) {
			try {
				return objectMapper.readValue(cache, AccommodationResult.class);
			} catch (JsonProcessingException e) {
				log.warn("Failed to deserialize cache for productId={}: {}", productId, e.getMessage());
			}
		}

		AccommodationResult accommodationResult = accommodationRepository.findByProductId(productId)
			.map(AccommodationResult::new)
			.orElseThrow(() -> new EntityNotFoundException(Accommodation.class, productId));

		try {
			accommodationCache.set(productId, objectMapper.writeValueAsString(accommodationResult));
		} catch (JsonProcessingException e) {
			log.warn("Failed to serialize ticket cache for productId={}: {}", productId, e.getMessage());
		}

		return accommodationResult;
	}

	@Override
	public ProductResult createProduct(ProductCreateCommand productCreateCommand) {
		AccommodationCreateCommand accommodationCreateCommand = (AccommodationCreateCommand)productCreateCommand;

		Accommodation accommodation = accommodationRepository.save(
			Accommodation.create(
				accommodationCreateCommand.getName(),
				accommodationCreateCommand.getDescription(),
				accommodationCreateCommand.getPrice(),
				accommodationCreateCommand.getQuantity(),
				accommodationCreateCommand.getPlace(),
				accommodationCreateCommand.getCheckInTime(),
				accommodationCreateCommand.getCheckOutTime()
			)
		);

		return new AccommodationResult(accommodation);
	}
}
