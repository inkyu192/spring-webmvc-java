package spring.web.java.dto.response;


import spring.web.java.domain.OrderItem;

public record OrderItemResponse(
        String itemName,
        int orderPrice,
        int count
) {
    public OrderItemResponse(OrderItem orderItem) {
        this(
                orderItem.getItem().getName(),
                orderItem.getOrderPrice(),
                orderItem.getCount()
        );
    }
}
