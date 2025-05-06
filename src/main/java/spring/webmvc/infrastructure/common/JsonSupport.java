package spring.webmvc.infrastructure.common;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
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
}
