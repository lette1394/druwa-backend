package me.druwa.be.domain.drama_review;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.druwa.be.domain.common.converter.DramaRatingConverter;

@Embeddable
@NoArgsConstructor
public class DramaPoint {
    private static final double MAX_RATE = 5.0;
    private static final double MIN_RATE = 0.0;

    @Getter
    @Column
    @Convert(converter = DramaRatingConverter.class)
    @PositiveOrZero
    private double point;

    public DramaPoint(final double point) {
        this.point = point;
        validate(point);
    }

    public static DramaPoint dramaPoint(final double point) {
        return new DramaPoint(point);
    }

    @JsonCreator
    public static DramaPoint parse(final String str) {
        final double value = Double.parseDouble(str);
        validate(value);
        return new DramaPoint(value);
    }

    private static void validate(final double point) {
        if (point < MIN_RATE || point > MAX_RATE) {
            throw new IllegalArgumentException(String.format("point exceed or under value : [%s]", point));
        }

        final double fractionPart = point % 1;
        if (0.499999999 > fractionPart || fractionPart > 0.500000001) {
            throw new IllegalArgumentException(String.format("invalid fraction part : [%s]", fractionPart));
        }
    }
}
