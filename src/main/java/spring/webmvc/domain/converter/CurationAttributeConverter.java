package spring.webmvc.domain.converter;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.vo.CurationAttribute;

@Component
@Converter
@RequiredArgsConstructor
public class CurationAttributeConverter implements AttributeConverter<CurationAttribute, String> {

	private final ObjectMapper objectMapper;

	@Override
	public String convertToDatabaseColumn(CurationAttribute attribute) {
		if (attribute == null) {
			return null;
		}

		try {
			return objectMapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public CurationAttribute convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return new CurationAttribute();
		}

		try {
			return objectMapper.readValue(dbData, CurationAttribute.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
