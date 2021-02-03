package sample.persistence.dto.converter;

import java.io.IOException;
import java.util.Map;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @created 2021/1/25
 * @author tom.lin(tom.lin @ linecorp.com)
 */
public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {
	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(Map<String, Object> attribute) {
		String customerInfoJson = null;
		try {
			customerInfoJson = objectMapper.writeValueAsString(attribute);
		} catch (final JsonProcessingException e) {
			System.out.println("JSON writing error");
		}

		return customerInfoJson;
	}

	@Override
	public Map<String, Object> convertToEntityAttribute(String dbData) {
		Map<String, Object> map = null;
		try {
			map = objectMapper.readValue(dbData, Map.class);
		} catch (final IOException e) {
			System.out.println("JSON reading error");
		}

		return map;
	}
}
