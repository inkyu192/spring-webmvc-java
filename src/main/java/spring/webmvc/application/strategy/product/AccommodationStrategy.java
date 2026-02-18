package spring.webmvc.application.strategy.product;

import org.springframework.stereotype.Component;

import spring.webmvc.application.dto.command.AccommodationPutCommand;
import spring.webmvc.application.dto.command.ProductAttributePutCommand;
import spring.webmvc.application.dto.result.AccommodationResult;
import spring.webmvc.application.dto.result.ProductAttributeResult;
import spring.webmvc.domain.model.entity.Accommodation;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.enums.ProductCategory;
import spring.webmvc.domain.repository.AccommodationRepository;
import spring.webmvc.infrastructure.exception.NotFoundEntityException;

@Component
public class AccommodationStrategy implements ProductAttributeStrategy {

	private final AccommodationRepository accommodationRepository;

	public AccommodationStrategy(AccommodationRepository accommodationRepository) {
		this.accommodationRepository = accommodationRepository;
	}

	@Override
	public ProductCategory getCategory() {
		return ProductCategory.ACCOMMODATION;
	}

	@Override
	public ProductAttributeResult findByProductId(Long productId) {
		Accommodation accommodation = accommodationRepository.findByProductId(productId)
			.orElseThrow(() -> new NotFoundEntityException(Accommodation.class, productId));

		return AccommodationResult.of(accommodation);
	}

	@Override
	public ProductAttributeResult create(Product product, ProductAttributePutCommand command) {
		AccommodationPutCommand accommodationCommand = (AccommodationPutCommand)command;

		Accommodation accommodation = Accommodation.create(
			product,
			accommodationCommand.place(),
			accommodationCommand.checkInTime(),
			accommodationCommand.checkOutTime()
		);

		accommodationRepository.save(accommodation);

		return AccommodationResult.of(accommodation);
	}

	@Override
	public ProductAttributeResult update(Long productId, ProductAttributePutCommand command) {
		AccommodationPutCommand accommodationCommand = (AccommodationPutCommand)command;

		Accommodation accommodation = accommodationRepository.findByProductId(productId)
			.orElseThrow(() -> new NotFoundEntityException(Accommodation.class, productId));

		accommodation.update(
			accommodationCommand.place(),
			accommodationCommand.checkInTime(),
			accommodationCommand.checkOutTime()
		);

		return AccommodationResult.of(accommodation);
	}

	@Override
	public void deleteProduct(Long productId) {
		Accommodation accommodation = accommodationRepository.findByProductId(productId)
			.orElseThrow(() -> new NotFoundEntityException(Accommodation.class, productId));

		accommodationRepository.delete(accommodation);
	}
}
