package spring.web.kotlin.domain.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import spring.web.kotlin.infrastructure.util.convertToKst
import spring.web.kotlin.infrastructure.util.convertToUtc
import java.time.LocalDateTime

@Converter(autoApply = true)
class LocalDateTimeConverter: AttributeConverter<LocalDateTime?, LocalDateTime?> {

    override fun convertToDatabaseColumn(attribute: LocalDateTime?) = attribute?.convertToUtc()

    override fun convertToEntityAttribute(dbData: LocalDateTime?) = dbData?.convertToKst()
}