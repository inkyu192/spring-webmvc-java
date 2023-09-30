package com.toy.shop.controller;

import com.toy.shop.dto.request.OrderSaveRequest;
import com.toy.shop.dto.response.OrderResponse;
import com.toy.shop.service.OrderService;
import com.toy.shop.dto.response.ApiResponse;
import com.toy.shop.domain.DeliveryStatus;
import com.toy.shop.domain.OrderStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Object saveOrder(@RequestBody @Valid OrderSaveRequest orderSaveRequest) {
        OrderResponse responseDto = orderService.saveOrder(orderSaveRequest);

        return new ApiResponse<>(responseDto);
    }

    @GetMapping
    public Object orders(@RequestParam Long memberId,
                         @RequestParam(required = false) OrderStatus orderStatus,
                         @RequestParam(required = false) DeliveryStatus deliveryStatus,
                         Pageable pageable) {
        Page<OrderResponse> list = orderService.orders(memberId, orderStatus, deliveryStatus, pageable);

        return new ApiResponse<>(list);
    }

    @GetMapping("/{id}")
    public Object order(@PathVariable Long id) {
        OrderResponse responseDto = orderService.order(id);

        return new ApiResponse<>(responseDto);
    }

    @PatchMapping("/{id}")
    public Object cancelOrder(@PathVariable Long id) {
        OrderResponse responseDto = orderService.cancelOrder(id);

        return new ApiResponse<>(responseDto);
    }
}
