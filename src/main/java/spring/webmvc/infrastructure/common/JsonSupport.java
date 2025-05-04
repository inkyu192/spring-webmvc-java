package spring.webmvc.infrastructure.common;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonSupport {

	private final ObjectMapper objectMapper;

	public <T> T readValue(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			log.error("Failed to deserialize [{}] to [{}]: {}", json, clazz.getSimpleName(), e.getMessage(), e);
			return null;
		}
	}

	public String writeValueAsString(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error("Failed to serialize [{}]: {}", obj.getClass().getSimpleName(), e.getMessage(), e);
			return null;
		}
	}

	public String extractField(String json, String fieldName) {
		try {
			JsonNode node = objectMapper.readTree(json);
			JsonNode fieldNode = node.get(fieldName);

			if (fieldNode == null || fieldNode.isNull()) {
				log.warn("Field '{}' not found", fieldName);
				return null;
			}

			return fieldNode.asText();
		} catch (JsonProcessingException e) {
			log.error("Failed to extract field '{}': {}", fieldName, e.getMessage(), e);
			return null;
		}
	}
}
