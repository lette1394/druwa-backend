package me.druwa.be.domain.common.converter;

import java.util.Objects;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import me.druwa.be.domain.common.model.PositiveOrZeroLong;

@Converter
public class PositiveOrZeroLongConverter implements AttributeConverter<PositiveOrZeroLong, Long> {

    @Override
    public Long convertToDatabaseColumn(PositiveOrZeroLong attribute) {
        if (Objects.isNull(attribute)) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public PositiveOrZeroLong convertToEntityAttribute(Long value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return new PositiveOrZeroLong(value);
    }
}