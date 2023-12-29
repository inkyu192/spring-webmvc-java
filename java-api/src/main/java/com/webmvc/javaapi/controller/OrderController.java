package com.webmvc.javaapi.controller;

import com.webmvc.javaapi.constant.DeliveryStatus;
import com.webmvc.javaapi.constant.OrderStatus;
import com.webmvc.javaapi.dto.request.OrderSaveRequest;
import com.webmvc.javaapi.dto.response.ApiResponse;
import com.webmvc.javaapi.dto.response.OrderResponse;
import com.webmvc.javaapi.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<OrderResponse> saveOrder(@RequestBody @Valid OrderSaveRequest orderSaveRequest) {
        OrderResponse responseDto = orderService.saveOrder(orderSaveRequest);

        return new ApiResponse<>(responseDto);
    }

    @GetMapping
    public ApiResponse<Page<OrderResponse>> findOrders(
            Pageable pageable,
            @RequestParam Long memberId,
            @RequestParam(required = false) OrderStatus orderStatus,
            @RequestParam(required = false) DeliveryStatus deliveryStatus
    ) {
        Page<OrderResponse> list = orderService.findOrders(memberId, orderStatus, deliveryStatus, pageable);

        return new ApiResponse<>(list);
    }

    @GetMapping("{id}")
    public ApiResponse<OrderResponse> findOrder(@PathVariable Long id) {
        OrderResponse responseDto = orderService.findOrder(id);

        return new ApiResponse<>(responseDto);
    }

    @PostMapping("{id}")
    public ApiResponse<OrderResponse> cancelOrder(@PathVariable Long id) {
        OrderResponse responseDto = orderService.cancelOrder(id);

        return new ApiResponse<>(responseDto);
    }
}
