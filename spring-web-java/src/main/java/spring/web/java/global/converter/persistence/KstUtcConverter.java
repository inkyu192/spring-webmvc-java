package spring.web.java.global.converter.persistence;

import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class KstUtcConverter implements AttributeConverter<LocalDateTime, LocalDateTime> {

	private static final ZoneId KST = ZoneId.of("Asia/Seoul");
	private static final ZoneId UTC = ZoneId.of("UTC");

	@Override
	public LocalDateTime convertToDatabaseColumn(LocalDateTime attribute) {
		if (attribute == null) {
			return null;
		}
		return attribute.atZone(KST).withZoneSameInstant(UTC).toLocalDateTime();
	}

	@Override
	public LocalDateTime convertToEntityAttribute(LocalDateTime dbData) {
		if (dbData == null) {
			return null;
		}
		return dbData.atZone(UTC).withZoneSameInstant(KST).toLocalDateTime();
	}
}
