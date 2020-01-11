package me.druwa.be.domain.drama.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.model.Timestamp;
import me.druwa.be.domain.user.model.User;

@Entity
@Table(name = "drama_like_")
@AllArgsConstructor
@NoArgsConstructor
public class DramaLike {
    @EmbeddedId
    private DramaLikeKey dramaLikeId;

    @ManyToOne
    @MapsId("drama_id")
    @JoinColumn(name = "drama_id")
    private Drama drama;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private Timestamp timestamp;

    @Embeddable
    public static class DramaLikeKey implements Serializable {
        @Column(name = "drama_id")
        private Long dramaId;

        @Column(name = "user_id")
        private Long userId;
    }
}
