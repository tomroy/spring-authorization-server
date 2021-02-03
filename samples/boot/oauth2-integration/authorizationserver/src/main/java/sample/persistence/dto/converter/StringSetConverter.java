package sample.persistence.dto.converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @created 2021/1/19
 * @author tom.lin(tom.lin @ linecorp.com)
 */
@Converter
public class StringSetConverter implements AttributeConverter<Set<String>, String> {
	private static final String SPLIT_CHAR = ",";

	@Override
	public String convertToDatabaseColumn(Set<String> stringSet) {
		return stringSet != null ? String.join(SPLIT_CHAR, stringSet) : null;
	}

	@Override
	public Set<String> convertToEntityAttribute(String string) {
		return string != null ? Arrays.stream(string.split(SPLIT_CHAR)).collect(Collectors.toSet()) :
			   Collections.emptySet();
	}
}
