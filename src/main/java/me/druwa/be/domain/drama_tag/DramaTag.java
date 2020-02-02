package me.druwa.be.domain.drama_tag;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.AssociationOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.druwa.be.domain.common.db.JoinTableName;
import me.druwa.be.domain.drama.model.Dramas;

@Entity
@Table(name = "drama_tag_")
@ToString(of = "tagName")
@EqualsAndHashCode(of = "tagName")
@NoArgsConstructor
@AllArgsConstructor
public class DramaTag {
    private static final int TAG_NAME_MIN_SIZE = 2;
    private static final int TAG_NAME_MAX_SIZE = 20;
    private static final String TAG_NAME_REGEX = "[a-zA-Z가-힣0-9]+";

    @Id
    @Column(name = "tag_name")
    @Size(min = TAG_NAME_MIN_SIZE, max = TAG_NAME_MAX_SIZE)
    @Pattern(regexp = TAG_NAME_REGEX)
    private String tagName;

    @Embedded
    @AssociationOverride(name = "dramas",
                         joinTable = @JoinTable(name = JoinTableName.DRAMA__HAS__DRAMA_TAG,
                                                joinColumns = @JoinColumn(name = "tag_name"),
                                                inverseJoinColumns = @JoinColumn(name = "drama_id")))
    private Dramas dramas;

    public DramaTag(final String tagName) {
        this.tagName = tagName;
    }

    public static DramaTag dramaTag(final String tagName) {
        return new DramaTag(tagName);
    }

    public static class View {
        public static class Create {
            @Data
            public static class Request {
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
