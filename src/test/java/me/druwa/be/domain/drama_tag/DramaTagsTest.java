package me.druwa.be.domain.drama_tag;

import org.junit.jupiter.api.Test;

import static me.druwa.be.domain.drama_tag.DramaTags.dramaTags;
import static org.assertj.core.api.Assertions.assertThat;

class DramaTagsTest {

    @Test
    void equals() {
        assertThat(dramaTags("tag1")).isEqualTo(dramaTags("tag1"));
    }

    @Test
    void filter() {
        final DramaTags filtered = dramaTags("tag1", "willBeRemoved", "tag2")
                                           .filter(dramaTags("tag1", "tag2"));

        assertThat(filtered).isEqualTo(dramaTags("tag1", "tag2"));
    }
}