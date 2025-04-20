package spring.webmvc.application.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import spring.webmvc.domain.model.entity.Accommodation;
import spring.webmvc.domain.repository.AccommodationRepository;
import spring.webmvc.presentation.dto.request.AccommodationCreateRequest;
import spring.webmvc.presentation.dto.request.AccommodationUpdateRequest;
import spring.webmvc.presentation.dto.response.AccommodationResponse;
import spring.webmvc.presentation.exception.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceTest {

	@InjectMocks
	private AccommodationService accommodationService;

	@Mock
	private AccommodationRepository accommodationRepository;

	@Test
	@DisplayName("createAccommodation: Accommodation 저장하고 반환한다")
	void createAccommodation() {
		// Given
		AccommodationCreateRequest request = new AccommodationCreateRequest(
			"name",
			"description",
			1000,
			5,
			"place",
			Instant.now(),
			Instant.now().plus(1, ChronoUnit.DAYS)
		);
		Accommodation accommodation = Accommodation.create(
			"name",
			"description",
			1000,
			5,
			"place",
			Instant.now(),
			Instant.now().plus(1, ChronoUnit.DAYS)
		);

		Mockito.when(accommodationRepository.save(Mockito.any(Accommodation.class))).thenReturn(accommodation);

		// When
		AccommodationResponse response = accommodationService.createAccommodation(request);

		// Then
		Assertions.assertThat(request.name()).isEqualTo(response.name());
		Assertions.assertThat(request.description()).isEqualTo(response.description());
		Assertions.assertThat(request.price()).isEqualTo(response.price());
		Assertions.assertThat(request.quantity()).isEqualTo(response.quantity());
		Assertions.assertThat(request.place()).isEqualTo(response.place());
	}

	@Test
	@DisplayName("findAccommodation: Accommodation 없을 경우 EntityNotFoundException 발생한다")
	void findAccommodationCase1() {
		// Given
		Long accommodationId = 1L;

		Mockito.when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.empty());

		// When & Then
		Assertions.assertThatThrownBy(() -> accommodationService.findAccommodation(accommodationId))
			.isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("findAccommodation: Accommodation 있을 경우 조회 후 반환한다")
	void findAccommodationCase2() {
		// Given
		Long accommodationId = 1L;
		Accommodation accommodation = Accommodation.create(
			"name",
			"description",
			1000,
			5,
			"place",
			Instant.now(),
			Instant.now().plus(1, ChronoUnit.DAYS)
		);

		Mockito.when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.of(accommodation));

		// When
		AccommodationResponse response = accommodationService.findAccommodation(accommodationId);

		// Then
		Assertions.assertThat(accommodation.getProduct().getName()).isEqualTo(response.name());
		Assertions.assertThat(accommodation.getProduct().getDescription()).isEqualTo(response.description());
		Assertions.assertThat(accommodation.getProduct().getPrice()).isEqualTo(response.price());
		Assertions.assertThat(accommodation.getProduct().getQuantity()).isEqualTo(response.quantity());
		Assertions.assertThat(accommodation.getPlace()).isEqualTo(response.place());
	}

	@Test
	@DisplayName("updateAccommodation: Accommodation 없을 경우 EntityNotFoundException 발생한다")
	void updateAccommodationCase1() {
		// Given
		Long accommodationId = 1L;
		AccommodationUpdateRequest request = new AccommodationUpdateRequest(
			"New Hotel",
			"New description",
			2000,
			10,
			"New Hotel",
			Instant.now(),
			Instant.now().plus(1, ChronoUnit.DAYS)
		);

		Mockito.when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.empty());

		// When & Then
		Assertions.assertThatThrownBy(() -> accommodationService.updateAccommodation(accommodationId, request))
			.isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("updateAccommodation: Accommodation 있을 경우 수정 후 반환한다")
	void updateAccommodationCase2() {
		// Given
		Long accommodationId = 1L;
		AccommodationUpdateRequest request = new AccommodationUpdateRequest(
			"name",
			"description",
			1000,
			5,
			"place",
			Instant.now(),
			Instant.now().plus(1, ChronoUnit.DAYS)
		);
		Accommodation accommodation = Accommodation.create(
			"name",
			"description",
			1000,
			5,
			"place",
			Instant.now(),
			Instant.now().plus(1, ChronoUnit.DAYS)
		);

		Mockito.when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.of(accommodation));

		// When
		AccommodationResponse response = accommodationService.updateAccommodation(accommodationId, request);

		// Then
		Assertions.assertThat(request.name()).isEqualTo(response.name());
		Assertions.assertThat(request.description()).isEqualTo(response.description());
		Assertions.assertThat(request.price()).isEqualTo(response.price());
		Assertions.assertThat(request.quantity()).isEqualTo(response.quantity());
		Assertions.assertThat(request.place()).isEqualTo(response.place());

		Mockito.verify(accommodationRepository, Mockito.never()).save(Mockito.any());
	}

	@Test
	@DisplayName("deleteAccommodation: Accommodation 없을 경우 EntityNotFoundException 발생한다")
	void deleteAccommodationCase1() {
		// Given
		Long accommodationId = 1L;

		Mockito.when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.empty());

		// When & Then
		Assertions.assertThatThrownBy(() -> accommodationService.deleteAccommodation(accommodationId))
			.isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("deleteAccommodation: Accommodation 있을 경우 삭제한다")
	void deleteAccommodationCase2() {
		// Given
		Long accommodationId = 1L;
		Accommodation accommodation = Accommodation.create(
			"name",
			"description",
			1000,
			5,
			"place",
			Instant.now(),
			Instant.now().plus(1, ChronoUnit.DAYS)
		);

		Mockito.when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.of(accommodation));

		// When
		accommodationService.deleteAccommodation(accommodationId);

		// Then
		Mockito.verify(accommodationRepository, Mockito.times(1)).delete(accommodation);
	}
}