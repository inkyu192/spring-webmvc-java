package spring.webmvc.application.strategy.product;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.command.ProductAttributePutCommand;
import spring.webmvc.application.dto.command.TransportPutCommand;
import spring.webmvc.application.dto.result.ProductAttributeResult;
import spring.webmvc.application.dto.result.TransportResult;
import spring.webmvc.domain.model.entity.Product;
import spring.webmvc.domain.model.entity.Transport;
import spring.webmvc.domain.model.enums.ProductCategory;
import spring.webmvc.domain.repository.TransportRepository;
import spring.webmvc.infrastructure.exception.NotFoundEntityException;

@Component
@RequiredArgsConstructor
public class TransportStrategy implements ProductAttributeStrategy {

	private final TransportRepository transportRepository;

	@Override
	public ProductCategory getCategory() {
		return ProductCategory.TRANSPORT;
	}

	@Override
	public ProductAttributeResult findByProductId(Long productId) {
		Transport transport = transportRepository.findByProductId(productId)
			.orElseThrow(() -> new NotFoundEntityException(Transport.class, productId));

		return TransportResult.of(transport);
	}

	@Override
	public ProductAttributeResult create(Product product, ProductAttributePutCommand command) {
		TransportPutCommand transportCommand = (TransportPutCommand)command;

		Transport transport = Transport.create(
			product,
			transportCommand.departureLocation(),
			transportCommand.arrivalLocation(),
			transportCommand.departureTime(),
			transportCommand.arrivalTime()
		);

		transportRepository.save(transport);

		return TransportResult.of(transport);
	}

	@Override
	public ProductAttributeResult update(Long productId, ProductAttributePutCommand command) {
		TransportPutCommand transportCommand = (TransportPutCommand)command;

		Transport transport = transportRepository.findByProductId(productId)
			.orElseThrow(() -> new NotFoundEntityException(Transport.class, productId));

		transport.update(
			transportCommand.departureLocation(),
			transportCommand.arrivalLocation(),
			transportCommand.departureTime(),
			transportCommand.arrivalTime()
		);

		return TransportResult.of(transport);
	}

	@Override
	public void deleteProduct(Long productId) {
		Transport transport = transportRepository.findByProductId(productId)
			.orElseThrow(() -> new NotFoundEntityException(Transport.class, productId));

		transportRepository.delete(transport);
	}
}
