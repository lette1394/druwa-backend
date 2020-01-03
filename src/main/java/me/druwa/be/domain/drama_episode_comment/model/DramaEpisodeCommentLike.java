package me.druwa.be.domain.drama_episode_comment.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.converter.PositiveLongConverter;
import me.druwa.be.domain.common.model.PositiveOrZeroLong;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DramaEpisodeCommentLike {
    @Column
    @NotNull
    @Convert(converter = PositiveLongConverter.class)
    private PositiveOrZeroLong likeCount;

    @PrePersist
    public void onCreate() {
        likeCount = new PositiveOrZeroLong(0L);
    }

    public DramaEpisodeCommentLike doLike() {
        likeCount = likeCount.increase();
        return this;
    }

    public DramaEpisodeCommentLike doDislike() {
        likeCount = likeCount.decrease();
        return this;
    }

    public long sum() {
        return likeCount.getValue();
    }

    public View.Create.Response toResponse() {
        return View.Create.Response.builder()
                                   .like(sum())
                                   .build();
    }

    @Data
    public static class View {
        @Data
        public static class Create {
            @Data
            public static class Response {
                @Builder
                public Response(final long like) {
                    this.like = like;
                }

                private long like;
            }
        }
    }
}
