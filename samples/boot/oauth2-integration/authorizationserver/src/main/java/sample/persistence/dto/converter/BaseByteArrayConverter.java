package sample.persistence.dto.converter;

import javax.persistence.AttributeConverter;

import org.springframework.util.SerializationUtils;

/**
 * @created 2021/1/29
 * @author tom.lin(tom.lin @ linecorp.com)
 */
public abstract class BaseByteArrayConverter<T> implements AttributeConverter<T, byte[]> {

	@Override
	public byte[] convertToDatabaseColumn(T attribute) {
		return SerializationUtils.serialize(attribute);
	}

	@Override
	public T convertToEntityAttribute(byte[] dbData) {
		return (T) SerializationUtils.deserialize(dbData);
	}
}
