package spring.webmvc.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Accommodation;
import spring.webmvc.domain.repository.AccommodationRepository;
import spring.webmvc.infrastructure.persistence.AccommodationJpaRepository;

@Component
@RequiredArgsConstructor
public class AccommodationRepositoryAdapter implements AccommodationRepository {

	private final AccommodationJpaRepository jpaRepository;

	@Override
	public Optional<Accommodation> findById(Long id) {
		return jpaRepository.findById(id);
	}

	@Override
	public Optional<Accommodation> findByProductId(Long productId) {
		return jpaRepository.findByProductId(productId);
	}

	@Override
	public Accommodation save(Accommodation accommodation) {
		return jpaRepository.save(accommodation);
	}

	@Override
	public void delete(Accommodation accommodation) {
		jpaRepository.delete(accommodation);
	}
}