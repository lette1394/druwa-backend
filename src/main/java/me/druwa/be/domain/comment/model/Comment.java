package me.druwa.be.domain.comment.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import me.druwa.be.domain.common.converter.PositiveLongConverter;
import me.druwa.be.domain.common.model.PositiveOrZeroLong;
import me.druwa.be.domain.user.model.User;
import me.druwa.be.domain.common.model.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment_")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @Convert(converter = PositiveLongConverter.class)
    private PositiveOrZeroLong depth;

    @Column
    @NotBlank
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment next;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment prev;

    @ManyToOne
    private User writtenBy;

    @Embedded
    private Timestamp timestamp;

    public Comment append(final Comment newComment) {
        newComment.prev = this;
        this.next = newComment;
        return newComment;
    }

    public View.Create.Response toCreateResponse() {
        return View.Create.Response.builder()
                                   .id(id)
                                   .createdAt(timestamp.getCreatedAt())
                                   .build();
    }

    @Data
    public static class View {
        @Data
        public static class Create {
            @Data
            public static class Request {
                @NotNull
                private Long postId;

                @NotNull
                private PositiveOrZeroLong depth;

                @NotBlank
                private String contents;

                public Comment comment() {
                    return null;
                }

            }

            @Data
            public static class Response {
                @Builder
                public Response(@PositiveOrZero final Long id, @NotNull final LocalDateTime createdAt) {
                    this.id = id;
                    this.createdAt = createdAt;
                }

                @PositiveOrZero
                private Long id;

                @NotNull
                private LocalDateTime createdAt;
            }
        }
    }
}
