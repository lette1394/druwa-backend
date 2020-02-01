package me.druwa.be.domain.drama.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DramaSearchStrings {
    private final Set<String> words;

    private static final DramaSearchStrings EMTPY = new DramaSearchStrings(new HashSet<>());
    private static final String DELIMITER = " ";

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
        return words.stream();
    }
}
