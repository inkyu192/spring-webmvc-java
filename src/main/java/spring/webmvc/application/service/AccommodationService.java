package spring.webmvc.application.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.application.dto.AccommodationDto;
import spring.webmvc.domain.cache.AccommodationCache;
import spring.webmvc.domain.model.entity.Accommodation;
import spring.webmvc.domain.repository.AccommodationRepository;
import spring.webmvc.infrastructure.common.JsonSupport;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccommodationService {

	private final AccommodationRepository accommodationRepository;
	private final AccommodationCache accommodationCache;
	private final JsonSupport jsonSupport;

	public AccommodationDto findAccommodation(Long id) {
		Optional<AccommodationDto> optionalAccommodationDto = accommodationCache.get(id)
			.flatMap(value -> jsonSupport.readValue(value, AccommodationDto.class));

		if (optionalAccommodationDto.isPresent()) {
			return optionalAccommodationDto.get();
		}

		AccommodationDto accommodationDto = accommodationRepository.findById(id)
			.map(AccommodationDto::new)
			.orElseThrow(() -> new EntityNotFoundException(Accommodation.class, id));

		jsonSupport.writeValueAsString(accommodationDto).ifPresent(value -> accommodationCache.set(id, value));

		return accommodationDto;
	}

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
