package me.druwa.be.domain.common.converter;

import java.util.stream.Collectors;
import javax.persistence.AttributeConverter;

import org.springframework.core.convert.converter.Converter;
import me.druwa.be.domain.drama.model.DramaSearchStrings;

@javax.persistence.Converter(autoApply = true)
public class StringToDramaSearchStringsConverter implements Converter<String, DramaSearchStrings>,
                                                            AttributeConverter<DramaSearchStrings, String> {
    @Override
    public DramaSearchStrings convert(final String source) {
        if (source.isEmpty()) {
            return DramaSearchStrings.empty();
        }
        return DramaSearchStrings.parse(source);
    }

    @Override
    public String convertToDatabaseColumn(final DramaSearchStrings attribute) {
        return attribute.stream().collect(Collectors.joining(","));
    }

    @Override
    public DramaSearchStrings convertToEntityAttribute(final String dbData) {
        return DramaSearchStrings.parse(dbData);
    }
}
