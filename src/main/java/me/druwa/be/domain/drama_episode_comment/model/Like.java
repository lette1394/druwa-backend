package me.druwa.be.domain.drama_episode_comment.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.converter.PositiveOrZeroLongConverter;
import me.druwa.be.domain.common.model.PositiveOrZeroLong;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    @Column
    @NotNull
    @Convert(converter = PositiveOrZeroLongConverter.class)
    private PositiveOrZeroLong likeCount = PositiveOrZeroLong.ZERO;

    @PrePersist
    public void onCreate() {
        likeCount = new PositiveOrZeroLong(0L);
    }

    public Like doLike() {
        likeCount = likeCount.increase();
        return this;
    }

    public Like doDislike() {
        likeCount = likeCount.decrease();
        return this;
    }

    @JsonProperty("like")
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
            @Builder
            public static class Response {
                private Long like;
            }
        }
    }
}
