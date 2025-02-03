package spring.web.kotlin.global.converter.persistence

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import spring.web.kotlin.global.common.convertToKst
import spring.web.kotlin.global.common.convertToUtc
import java.time.LocalDateTime

@Converter(autoApply = true)
class LocalDateTimeConverter: AttributeConverter<LocalDateTime?, LocalDateTime?> {

    override fun convertToDatabaseColumn(attribute: LocalDateTime?) = attribute?.convertToUtc()

    override fun convertToEntityAttribute(dbData: LocalDateTime?) = dbData?.convertToKst()
}