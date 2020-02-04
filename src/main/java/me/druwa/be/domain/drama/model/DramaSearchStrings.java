package me.druwa.be.domain.drama.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.converter.CommaBasedSetStringLowercaseConverter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DramaSearchStrings {
    @Column
    @Convert(converter = CommaBasedSetStringLowercaseConverter.class)
    private Set<String> words = Sets.newHashSet();

    private static final DramaSearchStrings EMTPY = new DramaSearchStrings(new HashSet<>());
    private static final String DELIMITER = ",";

    public static DramaSearchStrings empty() {
        return EMTPY;
    }

    public static DramaSearchStrings parse(final String source) {
        return new DramaSearchStrings(Sets.newHashSet(source.split(DELIMITER)));
    }

    public boolean isEmpty() {
        return words.isEmpty();
    }

    public Stream<String> stream() {
        return words.stream()
                    .map(String::toLowerCase);
    }

    @Override
    public String toString() {
        return stream().collect(Collectors.joining(","));
    }
}
