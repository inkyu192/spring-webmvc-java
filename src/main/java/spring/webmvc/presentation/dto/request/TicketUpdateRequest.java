package spring.webmvc.presentation.dto.request;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import spring.webmvc.domain.model.enums.Category;

@Getter
public class TicketUpdateRequest extends ProductUpdateRequest {
    @NotBlank
    private final String place;
    @NotNull
    private final Instant performanceTime;
    @NotBlank
    private final String duration;
    @NotBlank
    private final String ageLimit;

    public TicketUpdateRequest(
        Category category,
        String name,
        String description,
        int price,
        int quantity,
        String place,
        Instant performanceTime,
        String duration,
        String ageLimit
    ) {
        super(category, name, description, price, quantity);
        this.place = place;
        this.performanceTime = performanceTime;
        this.duration = duration;
        this.ageLimit = ageLimit;
    }
}
