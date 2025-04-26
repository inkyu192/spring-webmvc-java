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
import spring.webmvc.presentation.exception.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceTest {

	@InjectMocks
	private AccommodationService accommodationService;

	@Mock
	private AccommodationRepository accommodationRepository;

	@Test
	@DisplayName("createAccommodation: Accommodation 저장 후 반환한다")
	void createAccommodation() {
		// Given
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String place = "place";
		Instant checkInTime = Instant.now();
		Instant checkOutTime = Instant.now().plus(1, ChronoUnit.DAYS);

		Accommodation accommodation = Accommodation.create(
			name,
			description,
			price,
			quantity,
			place,
			checkInTime,
			checkOutTime
		);

		Mockito.when(accommodationRepository.save(Mockito.any(Accommodation.class))).thenReturn(accommodation);

		// When
		Accommodation result = accommodationService.createAccommodation(
			name,
			description,
			price,
			quantity,
			place,
			checkInTime,
			checkOutTime
		);

		// Then
		Assertions.assertThat(name).isEqualTo(result.getProduct().getName());
		Assertions.assertThat(description).isEqualTo(result.getProduct().getDescription());
		Assertions.assertThat(price).isEqualTo(result.getProduct().getPrice());
		Assertions.assertThat(quantity).isEqualTo(result.getProduct().getQuantity());
		Assertions.assertThat(place).isEqualTo(result.getPlace());
		Assertions.assertThat(checkInTime).isEqualTo(result.getCheckInTime());
		Assertions.assertThat(checkOutTime).isEqualTo(result.getCheckOutTime());
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
		Accommodation result = accommodationService.findAccommodation(accommodationId);

		// Then
		Assertions.assertThat(accommodation.getProduct().getName()).isEqualTo(result.getProduct().getName());
		Assertions.assertThat(accommodation.getProduct().getDescription())
			.isEqualTo(result.getProduct().getDescription());
		Assertions.assertThat(accommodation.getProduct().getPrice()).isEqualTo(result.getProduct().getPrice());
		Assertions.assertThat(accommodation.getProduct().getQuantity()).isEqualTo(result.getProduct().getQuantity());
		Assertions.assertThat(accommodation.getPlace()).isEqualTo(result.getPlace());
		Assertions.assertThat(accommodation.getCheckInTime()).isEqualTo(result.getCheckInTime());
		Assertions.assertThat(accommodation.getCheckOutTime()).isEqualTo(result.getCheckOutTime());
	}

	@Test
	@DisplayName("updateAccommodation: Accommodation 없을 경우 EntityNotFoundException 발생한다")
	void updateAccommodationCase1() {
		// Given
		Long accommodationId = 1L;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String place = "place";
		Instant checkInTime = Instant.now();
		Instant checkOutTime = Instant.now().plus(1, ChronoUnit.DAYS);

		Mockito.when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.empty());

		// When & Then
		Assertions.assertThatThrownBy(() ->
				accommodationService.updateAccommodation(
					accommodationId,
					name,
					description,
					price,
					quantity,
					place,
					checkInTime,
					checkOutTime
				)
			)
			.isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("updateAccommodation: Accommodation 있을 경우 수정 후 반환한다")
	void updateAccommodationCase2() {
		// Given
		Long accommodationId = 1L;
		String name = "name";
		String description = "description";
		int price = 1000;
		int quantity = 5;
		String place = "place";
		Instant checkInTime = Instant.now();
		Instant checkOutTime = Instant.now().plus(1, ChronoUnit.DAYS);

		Accommodation accommodation = Accommodation.create(
			name,
			description,
			price,
			quantity,
			place,
			checkInTime,
			checkOutTime
		);

		Mockito.when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.of(accommodation));

		// When
		Accommodation result = accommodationService.updateAccommodation(
			accommodationId,
			name,
			description,
			price,
			quantity,
			place,
			checkInTime,
			checkOutTime
		);

		// Then
		Assertions.assertThat(name).isEqualTo(result.getProduct().getName());
		Assertions.assertThat(description).isEqualTo(result.getProduct().getDescription());
		Assertions.assertThat(price).isEqualTo(result.getProduct().getPrice());
		Assertions.assertThat(quantity).isEqualTo(result.getProduct().getQuantity());
		Assertions.assertThat(place).isEqualTo(result.getPlace());
		Assertions.assertThat(checkInTime).isEqualTo(result.getCheckInTime());
		Assertions.assertThat(checkOutTime).isEqualTo(result.getCheckOutTime());

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