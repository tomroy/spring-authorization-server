package sample.persistence.dto.converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;

import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * @created 2021/1/20
 * @author tom.lin(tom.lin @ linecorp.com)
 */
public class AuthorizationGrantTypeSetConverter implements AttributeConverter<Set<AuthorizationGrantType>, String> {

	private static final String SPLIT_CHAR = ",";

	@Override
	public String convertToDatabaseColumn(Set<AuthorizationGrantType> attribute) {
		return attribute != null ? String.join(SPLIT_CHAR, attribute.stream().map(AuthorizationGrantType::getValue)
																	.collect(Collectors.toSet())) : null;
	}

	@Override
	public Set<AuthorizationGrantType> convertToEntityAttribute(String dbData) {
		return dbData != null ? Arrays.stream(dbData.split(SPLIT_CHAR)).map(AuthorizationGrantType::new).collect(
				Collectors.toSet()) : Collections.emptySet();
	}
}
