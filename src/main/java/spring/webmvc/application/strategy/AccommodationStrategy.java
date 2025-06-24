package spring.webmvc.application.strategy;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.application.dto.command.AccommodationCreateCommand;
import spring.webmvc.application.dto.command.AccommodationUpdateCommand;
import spring.webmvc.application.dto.command.ProductCreateCommand;
import spring.webmvc.application.dto.command.ProductUpdateCommand;
import spring.webmvc.application.dto.result.AccommodationResult;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.domain.cache.CacheKey;
import spring.webmvc.domain.cache.ValueCache;
import spring.webmvc.domain.model.entity.Accommodation;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.domain.repository.AccommodationRepository;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccommodationStrategy implements ProductStrategy {

	private final ValueCache valueCache;
	private final AccommodationRepository accommodationRepository;

	@Override
	public boolean supports(Category category) {
		return category == Category.ACCOMMODATION;
	}

	@Override
	public ProductResult findByProductId(Long productId) {
		String key = CacheKey.ACCOMMODATION.generate(productId);
		AccommodationResult cache = valueCache.get(key, AccommodationResult.class);

		if (cache != null) {
			return cache;
		}

		AccommodationResult accommodationResult = accommodationRepository.findByProductId(productId)
			.map(AccommodationResult::new)
			.orElseThrow(() -> new EntityNotFoundException(Accommodation.class, productId));

		valueCache.set(key, accommodationResult, CacheKey.ACCOMMODATION.getTimeout());

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

	@Override
	public ProductResult updateProduct(Long productId, ProductUpdateCommand productUpdateCommand) {
		AccommodationUpdateCommand accommodationUpdateCommand = (AccommodationUpdateCommand)productUpdateCommand;

		Accommodation accommodation = accommodationRepository.findByProductId(productId)
			.orElseThrow(() -> new EntityNotFoundException(Accommodation.class, productId));

		accommodation.update(
			accommodationUpdateCommand.getName(),
			accommodationUpdateCommand.getDescription(),
			accommodationUpdateCommand.getPrice(),
			accommodationUpdateCommand.getQuantity(),
			accommodationUpdateCommand.getPlace(),
			accommodationUpdateCommand.getCheckInTime(),
			accommodationUpdateCommand.getCheckOutTime()
		);

		return new AccommodationResult(accommodation);
	}

	@Override
	public void deleteProduct(Long productId) {
		Accommodation accommodation = accommodationRepository.findByProductId(productId)
			.orElseThrow(() -> new EntityNotFoundException(Accommodation.class, productId));

		accommodationRepository.delete(accommodation);

		String key = CacheKey.ACCOMMODATION.generate(productId);
		valueCache.delete(key);
	}
}
