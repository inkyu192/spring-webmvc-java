package spring.webmvc.presentation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record TicketCreateRequest(
    @NotBlank
    String name,
    @NotBlank
    String description,
    @Min(100)
    int price,
    @Max(9999)
    int quantity,
    @NotBlank
    String place,
    @NotNull
    Instant performanceTime,
    @NotBlank
    String duration,
    @NotBlank
    String ageLimit
) {
}