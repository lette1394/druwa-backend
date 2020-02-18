package me.druwa.be.domain.drama_review;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import me.druwa.be.domain.common.model.Timestamp;
import me.druwa.be.domain.drama.model.Drama;
import me.druwa.be.domain.user.model.User;

@Entity
@Table(name = "drama_review_")
@ToString
@EqualsAndHashCode(of = "dramaReviewId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DramaReview {
    private static final int MIN_TITLE_SIZE = 2;
    private static final int MAX_TITLE_SIZE = 50;

    private static final int MIN_CONTENTS_SIZE = 2;
    private static final int MAX_CONTENTS_SIZE = 1000;

    @Id
    @Column(name = "drama_review_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long dramaReviewId;

    @Column
    @NotBlank
    @Size(min = MIN_TITLE_SIZE, max = MAX_TITLE_SIZE)
    private String title;

    @Column
    @NotBlank
    @Size(min = MIN_CONTENTS_SIZE, max = MAX_CONTENTS_SIZE)
    private String contents;

    @Embedded
    @NonNull
    @Valid
    private DramaPoint point;

    @Embedded
    @NonNull
    @Builder.Default
    private Timestamp timestamp = Timestamp.now();

    @ManyToOne
    @NotNull
    private User writtenBy;

    @ManyToOne
    @JoinColumn(name = "drama_id")
    private Drama drama;

    @PrePersist
    public void onCreate() {
        timestamp = Timestamp.now();
    }

    @PreUpdate
    public void onUpdate() {
        timestamp.onUpdate();
    }

    public View.Read.Response toReadResponse(final User user) {
        return View.Read.Response.builder()
                                 .dramaReviewId(dramaReviewId)
                                 .point(point)
                                 .title(title)
                                 .contents(contents)
                                 .timestamp(timestamp)
                                 .writtenByMe(writtenBy.equals(user))
                                 .user(writtenBy.toReadResponse())
                                 .build();
    }

    public static class View {
        public static class Create {
            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Request {
                @NonNull
                @Valid
                private Double point;

                @NotBlank
                @Size(min = MIN_TITLE_SIZE, max = MAX_TITLE_SIZE)
                private String title;

                @NotBlank
                @Size(min = MIN_CONTENTS_SIZE, max = MAX_CONTENTS_SIZE)
                private String contents;

                @Builder(builderMethodName = "toPartialDramaReview")
                public DramaReview toPartialDramaReviewBuilder(final Drama drama, final User writtenBy) {
                    return DramaReview.builder()
                                      .title(title)
                                      .contents(contents)
                                      .point(DramaPoint.parse(point))
                                      .writtenBy(writtenBy)
                                      .drama(drama)
                                      .build();
                }
            }
        }

        public static class Read {
            @Data
            @Builder
            public static class Response {
                @PositiveOrZero
                private Long dramaReviewId;

                @NonNull
                @Valid
                @JsonUnwrapped
                private DramaPoint point;

                @NotBlank
                @Size(min = MIN_TITLE_SIZE, max = MAX_TITLE_SIZE)
                private String title;

                @NotBlank
                @Size(min = MIN_CONTENTS_SIZE, max = MAX_CONTENTS_SIZE)
                private String contents;

                @NonNull
                @JsonUnwrapped
                private Timestamp timestamp;

                private Boolean writtenByMe;
                private User.View.Read.Response user;
            }
        }
    }
}
