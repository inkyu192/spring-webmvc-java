package spring.webmvc.domain.converter;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.enums.CurationLayout;
import spring.webmvc.domain.model.vo.CurationExposureAttribute;

@Component
@Converter
@RequiredArgsConstructor
public class CurationExposureAttributeConverter implements AttributeConverter<CurationExposureAttribute, String> {

	private final ObjectMapper objectMapper;

	@Override
	public String convertToDatabaseColumn(CurationExposureAttribute attribute) {
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
	public CurationExposureAttribute convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return new CurationExposureAttribute(CurationLayout.CAROUSEL);
		}

		try {
			return objectMapper.readValue(dbData, CurationExposureAttribute.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
