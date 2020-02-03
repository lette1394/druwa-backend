package me.druwa.be.domain.drama_review;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class DramaPoint {
    private static final double MAX_RATE = 5.0;
    private static final double MIN_RATE = 0.0;

    @JsonProperty
    @Getter
    @Column
    @PositiveOrZero
    private double point;

    @JsonCreator
    public DramaPoint(@JsonProperty final double point) {
        this.point = point;
        validate(point);
    }

    public static DramaPoint dramaPoint(final double point) {
        return new DramaPoint(point);
    }

    public static DramaPoint parse(final Double value) {
        validate(value);
        return new DramaPoint(value);
    }

    private static void validate(final double point) {
        if (point < MIN_RATE || point > MAX_RATE) {
            throw new IllegalArgumentException(String.format("point exceed or under value : [%s]", point));
        }

        final double fractionPart = point % 1;
        if (false == (fractionPart < 0.000000001 || (0.499999999 > fractionPart || fractionPart > 0.500000001))) {
            throw new IllegalArgumentException(String.format("invalid fraction part : [%s]", fractionPart));
        }
    }
}
