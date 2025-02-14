package spring.web.java.domain.converter;

import java.time.LocalDateTime;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import spring.web.java.infrastructure.util.DateTimeUtil;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, LocalDateTime> {

	@Override
	public LocalDateTime convertToDatabaseColumn(LocalDateTime attribute) {
		if (attribute == null) {
			return null;
		}
		return DateTimeUtil.convertKstToUtc(attribute);
	}

	@Override
	public LocalDateTime convertToEntityAttribute(LocalDateTime dbData) {
		if (dbData == null) {
			return null;
		}
		return DateTimeUtil.convertUtcToKst(dbData);
	}
}
