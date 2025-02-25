package spring.web.kotlin.presentation.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import spring.web.kotlin.domain.model.enums.OrderStatus

@Component
class OrderStatusConverter : Converter<String, OrderStatus> {
    override fun convert(source: String) = OrderStatus.of(source)
}