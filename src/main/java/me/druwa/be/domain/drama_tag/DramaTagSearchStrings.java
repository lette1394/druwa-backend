package me.druwa.be.domain.drama_tag;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DramaTagSearchStrings {
    private final Set<String> words;

    private static final DramaTagSearchStrings EMTPY = new DramaTagSearchStrings(new HashSet<>());
    private static final String DELIMITER = ",";

    public static DramaTagSearchStrings empty() {
        return EMTPY;
    }

    public static DramaTagSearchStrings parse(final String source) {
        return new DramaTagSearchStrings(Sets.newHashSet(source.split(DELIMITER)));
    }

    public boolean isEmpty() {
        return words.isEmpty();
    }

    public Stream<String> stream() {
        return words.stream();
    }

    public DramaTags toTags() {
        return DramaTags.dramaTags(stream().map(DramaTag::dramaTag)
                                           .collect(Collectors.toSet()));
    }
}
