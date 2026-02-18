package spring.webmvc.domain.repository;

import java.util.Optional;

import spring.webmvc.domain.model.entity.Transport;

public interface TransportRepository {
	Optional<Transport> findByProductId(Long productId);

	Transport save(Transport transport);

	void delete(Transport transport);
}
