package me.druwa.be.domain.drama.model;

import java.util.stream.Stream;

import lombok.Builder;
import me.druwa.be.domain.drama_tag.DramaTagSearchStrings;

@Builder
public class DramaSearchQuery {
    private final DramaSearchStrings dramaSearchStrings;
    private final DramaTagSearchStrings dramaTagSearchStrings;

    public Stream<String> tags() {
        return dramaTagSearchStrings.stream();
    }

    public Stream<String> titles() {
        return dramaSearchStrings.stream();
    }
}
