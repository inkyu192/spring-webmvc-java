package spring.webmvc.application.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Accommodation;
import spring.webmvc.domain.repository.AccommodationRepository;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccommodationService {

	private final AccommodationRepository accommodationRepository;

	@Transactional
	public Accommodation createAccommodation(
		String name,
		String description,
		int price,
		int quantity,
		String place,
		Instant checkTime,
		Instant checkOutTime
	) {
		return accommodationRepository.save(
			Accommodation.create(
				name,
				description,
				price,
				quantity,
				place,
				checkTime,
				checkOutTime
			)
		);
	}

	@Transactional
	public Accommodation updateAccommodation(
		Long id,
		String name,
		String description,
		int price,
		int quantity,
		String place,
		Instant checkTime,
		Instant checkOutTime
	) {
		Accommodation accommodation = accommodationRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Accommodation.class, id));

		accommodation.update(
			name,
			description,
			price,
			quantity,
			place,
			checkTime,
			checkOutTime
		);

		return accommodation;
	}

	@Transactional
	public void deleteAccommodation(Long id) {
		Accommodation accommodation = accommodationRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Accommodation.class, id));

		accommodationRepository.delete(accommodation);
	}
}
