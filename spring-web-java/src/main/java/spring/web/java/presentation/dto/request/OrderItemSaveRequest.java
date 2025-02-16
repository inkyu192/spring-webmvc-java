package spring.web.java.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record OrderItemSaveRequest(
        @NotNull
        Long itemId,
        int count
) {
}
