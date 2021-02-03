package sample.persistence.dto.converter;

import java.time.Duration;

import javax.persistence.AttributeConverter;

/**
 * @created 2021/1/20
 * @author tom.lin(tom.lin @ linecorp.com)
 */
public class DurationConverter implements AttributeConverter<Duration, String> {

	@Override
	public String convertToDatabaseColumn(Duration attribute) {
		return String.valueOf(attribute.getSeconds());
	}

	@Override
	public Duration convertToEntityAttribute(String dbData) {
		return Duration.ofSeconds(Long.parseLong(dbData));
	}
}
