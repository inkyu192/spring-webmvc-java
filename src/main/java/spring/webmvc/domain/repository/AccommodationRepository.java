package spring.webmvc.domain.repository;

import java.util.Optional;

import spring.webmvc.domain.model.entity.Accommodation;

public interface AccommodationRepository {

	Optional<Accommodation> findById(Long id);

	Accommodation save(Accommodation accommodation);

	void delete(Accommodation accommodation);
}