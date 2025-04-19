package spring.webmvc.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.entity.Accommodation;
import spring.webmvc.domain.repository.AccommodationRepository;
import spring.webmvc.presentation.dto.request.AccommodationCreateRequest;
import spring.webmvc.presentation.dto.request.AccommodationUpdateRequest;
import spring.webmvc.presentation.dto.response.AccommodationResponse;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccommodationService {

	private final AccommodationRepository accommodationRepository;

	public AccommodationResponse findAccommodation(Long id) {
		Accommodation accommodation = accommodationRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Accommodation.class, id));

		return new AccommodationResponse(accommodation);
	}

	@Transactional
	public AccommodationResponse createAccommodation(AccommodationCreateRequest accommodationCreateRequest) {
		Accommodation accommodation = accommodationRepository.save(
			Accommodation.create(
				accommodationCreateRequest.name(),
				accommodationCreateRequest.description(),
				accommodationCreateRequest.price(),
				accommodationCreateRequest.quantity(),
				accommodationCreateRequest.place(),
				accommodationCreateRequest.checkInTime(),
				accommodationCreateRequest.checkOutTime()
			)
		);

		return new AccommodationResponse(accommodation);
	}

	@Transactional
	public AccommodationResponse updateAccommodation(Long id, AccommodationUpdateRequest accommodationUpdateRequest) {
		Accommodation accommodation = accommodationRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Accommodation.class, id));

		accommodation.update(
			accommodationUpdateRequest.name(),
			accommodationUpdateRequest.description(),
			accommodationUpdateRequest.price(),
			accommodationUpdateRequest.quantity(),
			accommodationUpdateRequest.place(),
			accommodationUpdateRequest.checkInTime(),
			accommodationUpdateRequest.checkOutTime()
		);

		return new AccommodationResponse(accommodation);
	}

	@Transactional
	public void deleteAccommodation(Long id) {
		Accommodation accommodation = accommodationRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Accommodation.class, id));

		accommodationRepository.delete(accommodation);
	}
}
