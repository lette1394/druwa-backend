package me.druwa.be.domain.drama_tag;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import com.google.common.collect.Sets;

import static org.assertj.core.api.Assertions.assertThat;

class DramaTagsTest {

    @Test
    void equals() {
        assertThat(dramaTags("tag1")).isEqualTo(dramaTags("tag1"));
    }

    @Test
    void filter() {
        final DramaTags filtered = dramaTags("tag1", "will_be_removed", "tag2")
                                           .filter(dramaTags("tag1", "tag2"));

        assertThat(filtered).isEqualTo(dramaTags("tag1", "tag2"));
    }

    private DramaTags dramaTags(final String... str) {
        return DramaTags.dramaTags(Sets.newHashSet(str)
                                       .stream()
                                       .map(DramaTag::new)
                                       .collect(Collectors.toSet()));
    }
}