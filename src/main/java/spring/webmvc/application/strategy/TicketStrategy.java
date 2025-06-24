package spring.webmvc.application.strategy;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.application.dto.command.ProductCreateCommand;
import spring.webmvc.application.dto.command.ProductUpdateCommand;
import spring.webmvc.application.dto.command.TicketCreateCommand;
import spring.webmvc.application.dto.command.TicketUpdateCommand;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.application.dto.result.TicketResult;
import spring.webmvc.domain.cache.CacheKey;
import spring.webmvc.domain.cache.ValueCache;
import spring.webmvc.domain.model.entity.Ticket;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.domain.repository.TicketRepository;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketStrategy implements ProductStrategy {

	private final ValueCache valueCache;
	private final TicketRepository ticketRepository;

	@Override
	public boolean supports(Category category) {
		return category == Category.TICKET;
	}

	@Override
	public ProductResult findByProductId(Long productId) {
		String key = CacheKey.TICKET.generate(productId);
		TicketResult cache = valueCache.get(key, TicketResult.class);

		if (cache != null) {
			return cache;
		}

		TicketResult ticketResult = ticketRepository.findByProductId(productId)
			.map(TicketResult::new)
			.orElseThrow(() -> new EntityNotFoundException(Ticket.class, productId));

		valueCache.set(key, ticketResult, CacheKey.TICKET.getTimeout());

		return ticketResult;
	}

	@Override
	public ProductResult createProduct(ProductCreateCommand productCreateCommand) {
		TicketCreateCommand ticketCreateCommand = (TicketCreateCommand)productCreateCommand;

		Ticket ticket = ticketRepository.save(
			Ticket.create(
				ticketCreateCommand.getName(),
				ticketCreateCommand.getDescription(),
				ticketCreateCommand.getPrice(),
				ticketCreateCommand.getQuantity(),
				ticketCreateCommand.getPlace(),
				ticketCreateCommand.getPerformanceTime(),
				ticketCreateCommand.getDuration(),
				ticketCreateCommand.getAgeLimit()
			)
		);

		return new TicketResult(ticket);
	}

	@Override
	public ProductResult updateProduct(Long productId, ProductUpdateCommand productUpdateCommand) {
		TicketUpdateCommand ticketUpdateCommand = (TicketUpdateCommand)productUpdateCommand;

		Ticket ticket = ticketRepository.findByProductId(productId)
			.orElseThrow(() -> new EntityNotFoundException(Ticket.class, productId));

		ticket.update(
			ticketUpdateCommand.getName(),
			ticketUpdateCommand.getDescription(),
			ticketUpdateCommand.getPrice(),
			ticketUpdateCommand.getQuantity(),
			ticketUpdateCommand.getPlace(),
			ticketUpdateCommand.getPerformanceTime(),
			ticketUpdateCommand.getDuration(),
			ticketUpdateCommand.getAgeLimit()
		);

		return new TicketResult(ticket);
	}

	@Override
	public void deleteProduct(Long productId) {
		Ticket ticket = ticketRepository.findByProductId(productId)
			.orElseThrow(() -> new EntityNotFoundException(Ticket.class, productId));

		ticketRepository.delete(ticket);

		String key = CacheKey.TICKET.generate(productId);
		valueCache.delete(key);
	}
}
