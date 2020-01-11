package me.druwa.be.domain.drama_episode_comment.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.converter.PositiveOrZeroLongConverter;
import me.druwa.be.domain.common.model.PositiveOrZeroLong;
import me.druwa.be.domain.common.model.Timestamp;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    @Column
    @NotNull
    @Convert(converter = PositiveOrZeroLongConverter.class)
    private PositiveOrZeroLong likeCount = PositiveOrZeroLong.ZERO;

    @Column
    @NotNull
    @Embedded
    private Timestamp likeCountTimestamp;

    public void onCreate() {
        likeCount = new PositiveOrZeroLong(0L);
        likeCountTimestamp = new Timestamp();
    }

    public void onUpdate() {
        likeCountTimestamp.onUpdate();
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
