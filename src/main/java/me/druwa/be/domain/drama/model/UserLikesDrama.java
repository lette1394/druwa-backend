package me.druwa.be.domain.drama.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.db.JoinTableName;
import me.druwa.be.domain.common.model.Timestamp;
import me.druwa.be.domain.user.model.User;


@Entity
@Table(name = JoinTableName.USER__LIKES__DRAMA)
@EqualsAndHashCode(of = { "drama", "user" })
@NoArgsConstructor
@AllArgsConstructor
public class UserLikesDrama {

    @EmbeddedId
    private DramaLikeKey dramaLikeId;

    @ManyToOne
    @MapsId("dramaId")
    @JoinColumn(name = "drama_id")
    private Drama drama;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private Timestamp timestamp;

    @Builder
    public UserLikesDrama(final Drama drama, final User user) {
        this.drama = drama;
        this.user = user;
        this.dramaLikeId = new DramaLikeKey();
        this.timestamp = Timestamp.now();
    }

    @PreUpdate
    public void onUpdate() {
        if (Objects.isNull(timestamp)) {
            return;
        }
        timestamp.onUpdate();
    }

    @Embeddable
    @EqualsAndHashCode(of = { "dramaId", "userId" })
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DramaLikeKey implements Serializable {
        @Column(name = "drama_id")
        private Long dramaId;

        @Column(name = "user_id")
        private Long userId;
    }
}
