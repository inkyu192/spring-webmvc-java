package spring.web.kotlin.presentation.exception

import org.springframework.http.HttpStatus

class InsufficientQuantityException(productName: String, requestedQuantity: Int, availableStock: Int) :
    AbstractHttpException("$productName 상품의 재고가 부족합니다. (요청: $requestedQuantity, 재고: $availableStock)", HttpStatus.CONFLICT)