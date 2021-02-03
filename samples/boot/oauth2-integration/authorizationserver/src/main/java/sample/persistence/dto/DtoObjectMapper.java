package sample.persistence.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @created 2021/1/26
 * @author tom.lin(tom.lin @ linecorp.com)
 */
public final class DtoObjectMapper {
	public static final ObjectMapper mapper = new ObjectMapper();

	private DtoObjectMapper() {}
}
