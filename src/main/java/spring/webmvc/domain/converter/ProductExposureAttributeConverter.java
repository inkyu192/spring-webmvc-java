package spring.webmvc.domain.converter;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import spring.webmvc.domain.model.vo.ProductExposureAttribute;

@Component
@Converter
@RequiredArgsConstructor
public class ProductExposureAttributeConverter implements AttributeConverter<ProductExposureAttribute, String> {

	private final ObjectMapper objectMapper;

	@Override
	public String convertToDatabaseColumn(ProductExposureAttribute attribute) {
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
	public ProductExposureAttribute convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}

		try {
			return objectMapper.readValue(dbData, ProductExposureAttribute.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
