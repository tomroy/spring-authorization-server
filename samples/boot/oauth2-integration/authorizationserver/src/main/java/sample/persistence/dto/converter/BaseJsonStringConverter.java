package sample.persistence.dto.converter;

import java.io.IOException;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;

import sample.persistence.dto.DtoObjectMapper;

/**
 * @created 2021/1/28
 * @author tom.lin(tom.lin @ linecorp.com)
 */
public class BaseJsonStringConverter<T> implements AttributeConverter<T, String> {

	private final Class<T> clazz;

	public BaseJsonStringConverter(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public String convertToDatabaseColumn(T attribute) {
		String jsonString = null;
		try {
			jsonString = DtoObjectMapper.mapper.writeValueAsString(attribute);
		} catch (final JsonProcessingException e) {
			System.out.println(clazz.getName() + "JSON writing error: " + e);
		}

		return jsonString;
	}

	@Override
	public T convertToEntityAttribute(String dbData) {
		T target = null;
		try {
			target = DtoObjectMapper.mapper.readValue(dbData, clazz);
		} catch (final IOException e) {
			System.out.println(clazz.getName() + "JSON reading error: " + e);
		}

		return target;
	}
}
