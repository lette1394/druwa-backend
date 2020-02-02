package me.druwa.be.domain.drama.model;

import java.util.stream.Stream;

import lombok.Builder;
import me.druwa.be.domain.drama_tag.DramaTagSearchStrings;
import me.druwa.be.domain.drama_tag.DramaTags;

@Builder
public class DramaSearchQuery {
    private final DramaSearchStrings dramaSearchStrings;
    private final DramaTagSearchStrings dramaTagSearchStrings;

    public DramaTags tags() {
        return dramaTagSearchStrings.toTags();
    }

    public Stream<String> titles() {
        return dramaSearchStrings.stream();
    }
}
