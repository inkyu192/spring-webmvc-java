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
