package spring.web.java.global.converter.persistence;

import java.time.LocalDateTime;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import spring.web.java.global.common.DateTimeUtils;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, LocalDateTime> {

	@Override
	public LocalDateTime convertToDatabaseColumn(LocalDateTime attribute) {
		if (attribute == null) {
			return null;
		}
		return DateTimeUtils.convertKstToUtc(attribute);
	}

	@Override
	public LocalDateTime convertToEntityAttribute(LocalDateTime dbData) {
		if (dbData == null) {
			return null;
		}
		return DateTimeUtils.convertUtcToKst(dbData);
	}
}
