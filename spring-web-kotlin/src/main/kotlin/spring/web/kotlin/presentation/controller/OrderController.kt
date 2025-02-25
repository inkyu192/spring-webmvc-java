package spring.web.kotlin.presentation.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import spring.web.kotlin.application.service.OrderService
import spring.web.kotlin.domain.model.enums.DeliveryStatus
import spring.web.kotlin.domain.model.enums.OrderStatus
import spring.web.kotlin.infrastructure.aspect.RequestLock
import spring.web.kotlin.presentation.dto.request.OrderSaveRequest

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService
) {
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @RequestLock
    @ResponseStatus(HttpStatus.CREATED)
    fun saveOrder(@RequestBody @Validated orderSaveRequest: OrderSaveRequest) = orderService.saveOrder(orderSaveRequest)

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun findOrders(
        @PageableDefault pageable: Pageable,
        @RequestParam(required = false) memberId: Long?,
        @RequestParam(required = false) orderStatus: OrderStatus?,
        @RequestParam(required = false) deliveryStatus: DeliveryStatus?,
    ) = orderService.findOrders(memberId, orderStatus, deliveryStatus, pageable)

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    fun findOrder(@PathVariable id: Long) = orderService.findOrder(id)

    @PostMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @RequestLock
    fun cancelOrder(@PathVariable id: Long) = orderService.cancelOrder(id)
}