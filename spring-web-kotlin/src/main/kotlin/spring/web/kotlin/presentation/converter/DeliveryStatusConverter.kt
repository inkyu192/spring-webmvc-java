package spring.web.kotlin.presentation.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import spring.web.kotlin.domain.model.enums.DeliveryStatus

@Component
class DeliveryStatusConverter : Converter<String, DeliveryStatus> {
    override fun convert(source: String) = DeliveryStatus.of(source)
}