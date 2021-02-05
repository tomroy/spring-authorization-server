package sample.persistence.dto.converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @created 2021/2/3
 * @author tom.lin(tom.lin @ linecorp.com)
 */
public class SetOfGrantedAuthorityConverter implements AttributeConverter<Set<GrantedAuthority>, String> {

	private static final String SPLIT_CHAR = ",";

	@Override
	public String convertToDatabaseColumn(Set<GrantedAuthority> attribute) {
		return attribute != null ? String.join(SPLIT_CHAR, attribute.stream().map(GrantedAuthority::getAuthority)
																	.collect(Collectors.toSet())) : null;
	}

	@Override
	public Set<GrantedAuthority> convertToEntityAttribute(String dbData) {
		return dbData != null ? Arrays.stream(dbData.split(SPLIT_CHAR)).map(SimpleGrantedAuthority::new).collect(
				Collectors.toSet()) : Collections.emptySet();
	}
}
