package me.druwa.be.domain.common.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import me.druwa.be.domain.common.model.PositiveOrZeroLong;

@Converter
public class PositiveLongConverter implements AttributeConverter<PositiveOrZeroLong, Long> {

    @Override
    public Long convertToDatabaseColumn(PositiveOrZeroLong attribute) {
        return attribute.getValue();
    }

    @Override
    public PositiveOrZeroLong convertToEntityAttribute(Long value) {
        return new PositiveOrZeroLong(value);
    }
}