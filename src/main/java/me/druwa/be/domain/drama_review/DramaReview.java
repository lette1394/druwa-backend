package me.druwa.be.domain.drama_review;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import me.druwa.be.domain.common.model.Timestamp;
import me.druwa.be.domain.user.model.User;

@Entity
@Table(name = "drama_review_")
@ToString
@EqualsAndHashCode(of = "dramaReviewId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DramaReview {

    @Id
    @Column(name = "drama_review_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long dramaReviewId;

    @Column
    private String title;

    @Column
    private String contents;

    @Embedded
    @NonNull
    private DramaPoint point;

    @Embedded
    @NonNull
    private Timestamp timestamp;

    @ManyToOne
    @NotNull
    private User registeredBy;


    @PrePersist
    public void onCreate() {
        timestamp = Timestamp.now();
    }

    @PreUpdate
    public void onUpdate() {
        timestamp.onUpdate();
    }

}
