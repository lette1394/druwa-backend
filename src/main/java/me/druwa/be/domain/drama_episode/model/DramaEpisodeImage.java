package me.druwa.be.domain.drama_episode.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.druwa.be.domain.common.db.Image;
import me.druwa.be.domain.common.db.ImageType;
import me.druwa.be.domain.common.model.PositiveOrZeroLong;
import me.druwa.be.domain.common.model.Timestamp;

@Entity
@Table(name = "drama_episode_image_")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AttributeOverride(name = "imageId", column = @Column(name = "drama_episode_image_id"))
public class DramaEpisodeImage extends Image {
    @Builder
    public DramaEpisodeImage(final DramaEpisode dramaEpisode,
                             final Long imageId,
                             @NotBlank final String imageName,
                             @NotBlank final String imageKey,
                             final PositiveOrZeroLong widthPixel,
                             final PositiveOrZeroLong heightPixel,
                             final ImageType imageType,
                             final Timestamp timestamp) {
        super(imageId, imageName, imageKey, widthPixel, heightPixel, imageType, timestamp);
        this.dramaEpisode = dramaEpisode;
    }

    @ManyToOne
    @JoinColumn(name = "drama_episode_id")
    private DramaEpisode dramaEpisode;
}
