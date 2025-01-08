package spring.web.java.domain.order.dto;

import jakarta.validation.constraints.NotNull;

public record OrderItemSaveRequest(
        @NotNull
        Long itemId,
        int count
) {
}
