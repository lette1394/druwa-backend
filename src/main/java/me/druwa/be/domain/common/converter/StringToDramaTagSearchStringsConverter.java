package me.druwa.be.domain.common.converter;

import org.springframework.core.convert.converter.Converter;
import me.druwa.be.domain.drama_tag.DramaTagSearchStrings;

public class StringToDramaTagSearchStringsConverter implements Converter<String, DramaTagSearchStrings> {
    @Override
    public DramaTagSearchStrings convert(final String source) {
        if (source.isEmpty()) {
            return DramaTagSearchStrings.empty();
        }
        return DramaTagSearchStrings.parse(source);
    }
}
