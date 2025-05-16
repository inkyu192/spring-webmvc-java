package spring.webmvc.application.strategy;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.application.dto.command.ProductCreateCommand;
import spring.webmvc.application.dto.command.ProductUpdateCommand;
import spring.webmvc.application.dto.command.TicketCreateCommand;
import spring.webmvc.application.dto.command.TicketUpdateCommand;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.application.dto.result.TicketResult;
import spring.webmvc.domain.cache.KeyValueCache;
import spring.webmvc.domain.model.entity.Ticket;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.domain.repository.TicketRepository;
import spring.webmvc.domain.cache.CacheKey;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketStrategy implements ProductStrategy {

	private final KeyValueCache keyValueCache;
	private final TicketRepository ticketRepository;
	private final ObjectMapper objectMapper;

	@Override
	public boolean supports(Category category) {
		return category == Category.TICKET;
	}

	@Override
	public ProductResult findByProductId(Long productId) {
		String key = CacheKey.PRODUCT.generate(productId);
		String cache = keyValueCache.get(key);

		if (cache != null) {
			try {
				return objectMapper.readValue(cache, TicketResult.class);
			} catch (JsonProcessingException e) {
				log.warn("Failed to deserialize cache for productId={}: {}", productId, e.getMessage());
			}
		}

		TicketResult ticketResult = ticketRepository.findByProductId(productId)
			.map(TicketResult::new)
			.orElseThrow(() -> new EntityNotFoundException(Ticket.class, productId));

		try {
			keyValueCache.set(key, objectMapper.writeValueAsString(ticketResult), CacheKey.PRODUCT.getTimeout());
		} catch (JsonProcessingException e) {
			log.warn("Failed to serialize ticket cache for productId={}: {}", productId, e.getMessage());
		}

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

		String key = CacheKey.PRODUCT_STOCK.generate(ticket.getProduct());
		keyValueCache.set(key, String.valueOf(ticket.getProduct().getQuantity()));

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

		String key = CacheKey.PRODUCT_STOCK.generate(ticket.getProduct());
		keyValueCache.set(key, String.valueOf(ticket.getProduct().getQuantity()));

		return new TicketResult(ticket);
	}

	@Override
	public void deleteProduct(Long productId) {
		Ticket ticket = ticketRepository.findByProductId(productId)
			.orElseThrow(() -> new EntityNotFoundException(Ticket.class, productId));

		ticketRepository.delete(ticket);

		String stockKey = CacheKey.PRODUCT_STOCK.generate(ticket.getId());
		keyValueCache.delete(stockKey);
	}
}
