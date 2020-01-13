package me.druwa.be.domain.drama.model;

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
@Table(name = "drama_image_")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AttributeOverride(name = "imageId", column = @Column(name = "drama_image_id"))
public class DramaImage extends Image {

    @Builder
    public DramaImage(final Long imageId,
                      @NotBlank final String imageKey,
                      final PositiveOrZeroLong widthPixel,
                      final PositiveOrZeroLong heightPixel,
                      final ImageType imageType, final Timestamp timestamp) {
        super(imageId, imageKey, widthPixel, heightPixel, imageType, timestamp);
    }

    @ManyToOne
    @JoinColumn(name = "drama_id")
    private Drama drama;

    public static DramaImage dramaImage(final String imageKey) {
        return DramaImage.builder()
                         .imageKey(imageKey)
                         .build();
    }
}
