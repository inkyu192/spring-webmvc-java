package spring.webmvc.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Transport;
import spring.webmvc.domain.repository.TransportRepository;
import spring.webmvc.infrastructure.persistence.jpa.TransportJpaRepository;

@Component
@RequiredArgsConstructor
public class TransportRepositoryAdapter implements TransportRepository {

	private final TransportJpaRepository jpaRepository;

	@Override
	public Optional<Transport> findByProductId(Long productId) {
		return jpaRepository.findByProductId(productId);
	}

	@Override
	public Transport save(Transport transport) {
		return jpaRepository.save(transport);
	}

	@Override
	public void delete(Transport transport) {
		jpaRepository.delete(transport);
	}
}
