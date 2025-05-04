package spring.webmvc.infrastructure.common;

import java.util.Optional;

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

	public <T> Optional<T> readValue(String json, Class<T> clazz) {
		try {
			return Optional.of(objectMapper.readValue(json, clazz));
		} catch (JsonProcessingException e) {
			log.warn("Failed to deserialize [{}] to [{}]: {}", json, clazz.getSimpleName(), e.getMessage());
			return Optional.empty();
		}
	}

	public Optional<String> writeValueAsString(Object obj) {
		try {
			return Optional.of(objectMapper.writeValueAsString(obj));
		} catch (JsonProcessingException e) {
			log.warn("Failed to serialize [{}]: {}", obj.getClass().getSimpleName(), e.getMessage());
			return Optional.empty();
		}
	}
}
