package me.druwa.be.domain.common.converter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

@Converter
public class CommaBasedSetStringLowercaseConverter implements AttributeConverter<Set<String>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(final Set<String> attribute) {
        if (Objects.isNull(attribute)) {
            return StringUtils.EMPTY;
        }
        return attribute.stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public Set<String> convertToEntityAttribute(final String dbData) {
        if (StringUtils.isBlank(dbData)) {
            return Sets.newHashSet();
        }
        return Arrays.stream(dbData.split(DELIMITER))
                     .map(String::toLowerCase)
                     .collect(Collectors.toSet());
    }
}
