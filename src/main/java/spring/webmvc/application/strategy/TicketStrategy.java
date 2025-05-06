package spring.webmvc.application.strategy;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.result.ProductResult;
import spring.webmvc.application.dto.result.TicketResult;
import spring.webmvc.domain.cache.TicketCache;
import spring.webmvc.domain.model.entity.Ticket;
import spring.webmvc.domain.model.enums.Category;
import spring.webmvc.domain.repository.TicketRepository;
import spring.webmvc.infrastructure.common.JsonSupport;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Component
@RequiredArgsConstructor
public class TicketStrategy implements ProductStrategy {

	private final TicketCache ticketCache;
	private final TicketRepository ticketRepository;
	private final JsonSupport jsonSupport;

	@Override
	public boolean supports(Category category) {
		return category == Category.TICKET;
	}

	@Override
	public ProductResult findByProductId(Long productId) {
		String cache = ticketCache.get(productId);

		if (cache != null) {
			return jsonSupport.readValue(cache, TicketResult.class);
		}

		TicketResult ticketResult = ticketRepository.findByProductId(productId)
			.map(TicketResult::new)
			.orElseThrow(() -> new EntityNotFoundException(Ticket.class, productId));

		ticketCache.set(productId, jsonSupport.writeValueAsString(ticketResult));

		return ticketResult;
	}
}
