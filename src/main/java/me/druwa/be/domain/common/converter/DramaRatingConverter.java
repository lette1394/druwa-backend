package me.druwa.be.domain.common.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import me.druwa.be.domain.drama_review.DramaPoint;

@Converter
public class DramaRatingConverter implements AttributeConverter<DramaPoint, Double> {
    @Override
    public Double convertToDatabaseColumn(final DramaPoint attribute) {
        return attribute.getPoint();
    }

    @Override
    public DramaPoint convertToEntityAttribute(final Double dbData) {
        return new DramaPoint(dbData);
    }
}
