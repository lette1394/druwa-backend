package me.druwa.be.domain.drama.model;

import java.util.Objects;
import java.util.Set;
import javax.persistence.AssociationOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.model.IgnoreMerge;
import me.druwa.be.domain.common.model.Mergeable;
import me.druwa.be.domain.common.model.Timestamp;
import me.druwa.be.domain.drama_episode_comment.model.Like;
import me.druwa.be.domain.drama_tag.DramaTag;
import me.druwa.be.domain.drama_tag.DramaTags;
import me.druwa.be.domain.user.model.User;
import me.druwa.be.domain.user.model.Users;

@Entity
@Table(name = "drama_")
@EqualsAndHashCode(of = "dramaId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Drama implements Mergeable<Drama> {
    private static final int TITLE_MIN_LENGTH = 2;
    private static final int TITLE_MAX_LENGTH = 50;
    private static final int SUMMARY_MAX_LENGTH = 500;
    private static final int PRODUCT_COMPANY_MAX_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @IgnoreMerge
    @Getter
    @Column(name = "drama_id")
    private Long dramaId;

    @Column
    @NotBlank
    @Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH)
    private String title;

    @Column
    @Size(max = SUMMARY_MAX_LENGTH)
    private String summary;

    @Column
    @NotBlank
    @Size(max = PRODUCT_COMPANY_MAX_LENGTH)
    private String productionCompany;

    @Embedded
    private DramaImage dramaImage;

    @Embedded
    private Like dramaLike;

    @Embedded
    @AssociationOverride(name = "users",
                         joinColumns = @JoinColumn(name = "user_id"),
                         joinTable = @JoinTable(name = "drama_like_"))
    private Users likeUsers;

    @NotNull
    @ManyToOne
    private User registeredBy;

    @ManyToMany
    private Set<DramaTag> dramaTags;

    @NotNull
    @Embedded
    private Timestamp timestamp;

    public Like doLike(final User user) {
        if (likeUsers.contains(user)) {
            return dramaLike;
        }
        likeUsers.add(user);
        return dramaLike.doLike();
    }

    public Like doDislike(final User user) {
        if (likeUsers.contains(user)) {
            likeUsers.remove(user);
            return dramaLike.doDislike();
        }
        return dramaLike;
    }

    // TODO: 성능 개선 지점
    public Drama update(final DramaTags dramaTags) {
        setDramaTags(getDramaTags().merge(dramaTags));
        return this;
    }

    public Drama populateImageKey(final String key) {
        dramaImage = new DramaImage(key);
        return this;
    }

    public Drama populateUser(final User user) {
        registeredBy = user;
        return this;
    }

    @PrePersist
    public void onCreate() {
        timestamp = Timestamp.now();
        dramaLike = new Like();
    }

    @PreUpdate
    public void onUpdate() {
        timestamp.onUpdate();
    }

    @PostLoad
    public void onLoad() {
        if (Objects.isNull(dramaImage)) {
            dramaImage = new NullDramaImage();
        }
    }

    public View.Create.Response toCreateResponse() {
        return View.Create.Response.builder()
                                   .dramaId(dramaId)
                                   .build();
    }

    public View.Read.Response toReadResponse() {
        return View.Read.Response.builder()
                                 .dramaId(dramaId)
                                 .title(title)
                                 .like(dramaLike)
                                 .productionCompany(productionCompany)
                                 .imageUrl(dramaImage.toS3Url())
                                 .summary(summary)
                                 .timestamp(timestamp)
                                 .build();
    }

    @Data
    public static class View {
        @Data
        public static class Create {
            @Data
            @EqualsAndHashCode(callSuper = true)
            public static class MultipartRequest extends Request {
                @NotNull
                private MultipartFile image;

                public Drama toPartialDrama() {
                    return Drama.builder()
                                .title(title)
                                .productionCompany(productionCompany)
                                .summary(summary)
                                .build();
                }
            }

            @Data
            public static class Request {
                @NotBlank
                @Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH)
                protected String title;

                @NotBlank
                @Size(max = PRODUCT_COMPANY_MAX_LENGTH)
                protected String productionCompany;

                @NotBlank
                @Size(max = SUMMARY_MAX_LENGTH)
                protected String summary;

                public Drama toPartialDrama() {
                    return Drama.builder()
                                .title(title)
                                .productionCompany(productionCompany)
                                .summary(summary)
                                .build();
                }
            }

            @Data
            @Builder
            public static class Response {
                private Long dramaId;
            }
        }

        @Data
        public static class Read {
            @Data
            @Builder
            public static class Response {
                private Long dramaId;
                private String title;
                private String summary;
                private String productionCompany;
                private String imageUrl;
                @JsonUnwrapped
                private Like like;
                @JsonUnwrapped
                private Timestamp timestamp;
            }
        }

        @Data
        public static class Patch {
            @Data
            public static class Request {
                @Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH)
                private String title;

                @Size(max = PRODUCT_COMPANY_MAX_LENGTH)
                private String productionCompany;

                @Size(max = SUMMARY_MAX_LENGTH)
                private String summary;

                public Drama toPartialDrama() {
                    return Drama.builder()
                                .title(title)
                                .productionCompany(productionCompany)
                                .summary(summary)
                                .build();
                }
            }
        }
    }

    private DramaTags getDramaTags() {
        return DramaTags.dramaTags(dramaTags);
    }

    public void setDramaTags(final DramaTags dramaTags) {
        this.dramaTags = dramaTags.raw();
    }
}
