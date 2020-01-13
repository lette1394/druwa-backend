package me.druwa.be.domain.drama_tag;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import me.druwa.be.util.AutoSpringBootTest;
import me.druwa.be.util.PostgreSQLTestContainer;

import static me.druwa.be.domain.drama_tag.DramaTags.dramaTags;
import static org.assertj.core.api.Assertions.assertThat;

@AutoSpringBootTest
public class DramaTagServiceTest extends PostgreSQLTestContainer {

    @Autowired
    private DramaTagService dramaTagService;

    @Autowired
    private DramaTagRepository dramaTagRepository;

    @Test
    void createIfNotExists() {
        dramaTagRepository.saveAll(dramaTags("tag1", "willBeRemoved", "tag2"));
        final DramaTags filtered = dramaTagService.createIfNotExists(dramaTags("tag1", "tag2", "tag3"));

        assertThat(dramaTagRepository.findAll().size()).isEqualTo(4);
        assertThat(filtered).isEqualTo(dramaTags("tag1", "tag2", "tag3"));
    }
}

