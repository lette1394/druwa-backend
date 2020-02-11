package me.druwa.be.domain.drama.model;

import java.util.Objects;
import java.util.Optional;
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
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.druwa.be.domain.common.db.Image;
import me.druwa.be.domain.common.db.JoinTableName;
import me.druwa.be.domain.common.model.IgnoreMerge;
import me.druwa.be.domain.common.model.Mergeable;
import me.druwa.be.domain.common.model.MultipartImages;
import me.druwa.be.domain.common.model.Timestamp;
import me.druwa.be.domain.drama_episode.model.DramaEpisodes;
import me.druwa.be.domain.drama_review.DramaReviews;
import me.druwa.be.domain.drama_tag.DramaTags;
import me.druwa.be.domain.user.model.User;

import static me.druwa.be.domain.drama.model.DramaImages.dramaImages;

@Entity
@Table(name = "drama_")
@EqualsAndHashCode(of = "dramaId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = { "dramaId", "title" })
public class Drama implements Mergeable<Drama> {
    private static final int TITLE_MIN_LENGTH = 2;
    private static final int TITLE_MAX_LENGTH = 50;
    private static final int SUMMARY_MAX_LENGTH = 500;
    private static final int PRODUCT_COMPANY_MAX_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @IgnoreMerge
    @Getter
    private Long dramaId;

    @Getter
    @Column
    @NotBlank
    @Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH)
    private String title;

    @Column
    @Size(max = SUMMARY_MAX_LENGTH)
    private String summary;

    @Column
    private String extraSearchWords;

    @Column
    @NotBlank
    @Size(max = PRODUCT_COMPANY_MAX_LENGTH)
    private String productionCompany;

    @Embedded
    private DramaEpisodes dramaEpisodes;

    @Embedded
    private DramaImages dramaImages;

    @Embedded
    private DramaLikeOrDislike dramaLike;

    @NotNull
    @ManyToOne
    private User registeredBy;

    @Embedded
    @AssociationOverride(name = "dramaTags",
                         joinTable = @JoinTable(name = JoinTableName.DRAMA__HAS__DRAMA_TAG,
                                                joinColumns = @JoinColumn(name = "drama_id"),
                                                inverseJoinColumns = @JoinColumn(name = "tag_name")))
    private DramaTags dramaTags;

    @Embedded
    @AssociationOverride(name = "dramaReviews",
                         joinTable = @JoinTable(name = JoinTableName.USER__REVIEWS__DRAMA,
                                                joinColumns = @JoinColumn(name = "drama_id"),
                                                inverseJoinColumns = @JoinColumn(name = "drama_review_id")))
    private DramaReviews dramaReviews;

    @NotNull
    @Embedded
    private Timestamp timestamp;

    public Drama like(final User user) {
        dramaLike.doLike(this, user);
        return this;
    }

    public Drama dislike(final User user) {
        dramaLike.doDislike(this, user);
        return this;
    }

    public Drama update(final DramaTags dramaTags) {
        this.dramaTags.update(dramaTags);
        return this;
    }

    public Drama update(final DramaEpisodes dramaEpisodes) {
        this.dramaEpisodes.update(dramaEpisodes);
        return this;
    }

    public Drama merge(final DramaImages dramaImages) {
        this.dramaImages.merge(dramaImages);
        return this;
    }

    public Drama populateUser(final User user) {
        registeredBy = user;
        return this;
    }

    public Optional<DramaImage> image(final String imageName) {
        return dramaImages.stream()
                          .filter(image -> image.equalsName(imageName))
                          .findFirst();
    }

    @PrePersist
    public void onCreate() {
        timestamp = Timestamp.now();
        dramaLike = new DramaLikeOrDislike();
    }

    @PreUpdate
    public void onUpdate() {
        timestamp.onUpdate();
    }

    @PostLoad
    public void onLoad() {
        if (Objects.isNull(dramaImages)) {
            dramaImages = dramaImages();
        }
    }

    public View.Create.Response toCreateResponse() {
        return View.Create.Response.builder()
                                   .dramaId(dramaId)
                                   .build();
    }

    public View.Read.Response toReadResponse(final User user) {
        return View.Read.Response.builder()
                                 .dramaId(dramaId)
                                 .title(title)
                                 .likeOrDislike(dramaLike.toResponse(this, user))
                                 .productionCompany(productionCompany)
                                 .images(dramaImages.toResponse())
                                 .summary(summary)
                                 .timestamp(timestamp)
                                 .build();
    }

    public View.Search.Response toSearchResponse() {
        return View.Search.Response.builder()
                                   .dramaId(dramaId)
                                   .productionCompany(productionCompany)
                                   .images(dramaImages.toResponse())
                                   .title(title)
                                   .build();
    }

    public Set<Image.View.Read.Response> toImageOnlyResponse() {
        return dramaImages.toResponse();
    }

    public DramaLikeOrDislike.View.Read.Response toLikeResponse(final User user) {
        return dramaLike.toResponse(this, user);
    }

    @Data
    public static class View {
        @Data
        public static class Create {
            @Data
            @EqualsAndHashCode(callSuper = true)
            public static class MultipartRequest extends Request {
                @NotNull
                @Valid
                private MultipartImages images;
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

                protected DramaSearchStrings extraSearchWords = DramaSearchStrings.empty();

                public Drama toPartialDrama() {
                    return Drama.builder()
                                .title(title)
                                .productionCompany(productionCompany)
                                .summary(summary)
                                .extraSearchWords(extraSearchWords.toString())
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
                private Set<Image.View.Read.Response> images;
                @JsonUnwrapped
                private DramaLikeOrDislike.View.Read.Response likeOrDislike;
                @JsonUnwrapped
                private Timestamp timestamp;
            }
        }

        public static class Search {
            @Data
            @Builder
            public static class Response {
                private Long dramaId;
                private String title;
                private String productionCompany;
                private Set<Image.View.Read.Response> images;
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

                private DramaSearchStrings extraSearchWords = DramaSearchStrings.empty();

                public Drama toPartialDrama() {
                    return Drama.builder()
                                .title(title)
                                .productionCompany(productionCompany)
                                .summary(summary)
                                .extraSearchWords(extraSearchWords.toString())
                                .build();
                }
            }
        }
    }
}
