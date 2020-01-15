package me.druwa.be.domain.drama_review;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import me.druwa.be.domain.common.converter.DramaRatingConverter;

@Embeddable
@NoArgsConstructor
public class DramaPoint {
    private static final double MAX_RATE = 5.0;
    private static final double MIN_RATE = 0.0;

    @Getter
    @NonNull
    @Column
    @Convert(converter = DramaRatingConverter.class)
    private Double point;

    public DramaPoint(final Double point) {
        this.point = point;
        validate(point);
    }

    private static void validate(final double point) {
        if (point < MIN_RATE || point > MAX_RATE) {
            throw new IllegalArgumentException(String.format("point exceed or under value : [%s]", point));
        }
    }
}
