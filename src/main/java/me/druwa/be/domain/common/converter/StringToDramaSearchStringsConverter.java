package me.druwa.be.domain.common.converter;

import org.springframework.core.convert.converter.Converter;
import me.druwa.be.domain.drama.model.DramaSearchStrings;

public class StringToDramaSearchStringsConverter implements Converter<String, DramaSearchStrings> {
    @Override
    public DramaSearchStrings convert(final String source) {
        if (source.isEmpty()) {
            return DramaSearchStrings.empty();
        }
        return DramaSearchStrings.parse(source);
    }
}
