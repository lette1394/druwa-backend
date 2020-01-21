package me.druwa.be.domain.common.model;

import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Getter
@EqualsAndHashCode
public class PositiveOrZeroLong {
    public static PositiveOrZeroLong ZERO = new PositiveOrZeroLong(0L);
    public static PositiveOrZeroLong ONE = new PositiveOrZeroLong(1L);

    @PositiveOrZero
    private final Long value;

    @JsonCreator
    public static PositiveOrZeroLong parse(final String str) {
        final long value = Long.parseLong(str);
        validate(value);
        return new PositiveOrZeroLong(value);
    }

    public PositiveOrZeroLong(final Long value) {
        validate(value);
        this.value = value;
    }

    public static PositiveOrZeroLong positiveOrZeroLong(final Long value) {
        return new PositiveOrZeroLong(value);
    }

    public PositiveOrZeroLong increase() {
        return new PositiveOrZeroLong(value + 1);
    }

    public PositiveOrZeroLong decrease() {
        if (value == 0) {
            return ZERO;
        }
        return new PositiveOrZeroLong(value - 1);
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }

    @JsonValue
    public Long value() {
        return value;
    }

    private static void validate(final Long value) {
        if (value < 0) {
            throw new IllegalArgumentException(value + " is not positive");
        }
    }
}
