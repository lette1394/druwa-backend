package me.druwa.be.domain.drama_tag;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.drama.model.Drama;

@Entity
@EqualsAndHashCode(of = "tagName")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "drama_tag_")
public class DramaTag {
    private static final int TAG_NAME_MIN_SIZE = 2;
    private static final int TAG_NAME_MAX_SIZE = 20;
    private static final String TAG_NAME_REGEX = "[a-zA-Z가-힣0-9]+";


    @Id
    @Column
    @Size(min = TAG_NAME_MIN_SIZE, max = TAG_NAME_MAX_SIZE)
    @Pattern(regexp = TAG_NAME_REGEX)
    private String tagName;

    @ManyToMany(mappedBy = "dramaTags")
    private Set<Drama> dramas;

    public DramaTag(final String tagName) {
        this.tagName = tagName;
    }

    public static class View {
        public static class Create {
            @Data
            public static class Request {
                @NotEmpty
                private List<
                                    @Size(min = TAG_NAME_MIN_SIZE,
                                          max = TAG_NAME_MAX_SIZE)
                                    @Pattern(regexp = TAG_NAME_REGEX) String> tags;

                public DramaTags toPartialTags() {
                    return new DramaTags(this.tags.stream()
                                                  .map(DramaTag::new)
                                                  .collect(Collectors.toSet()));
                }
            }
        }
    }

    String raw() {
        return tagName;
    }
}
