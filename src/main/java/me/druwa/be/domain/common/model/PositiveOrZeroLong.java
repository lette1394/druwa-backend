package me.druwa.be.domain.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public class PositiveOrZeroLong {
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

    @JsonValue
    public String toString() {
        return String.valueOf(getValue());
    }

    private static void validate(final Long value) {
        if (value < 0) {
            throw new IllegalArgumentException(value + " is not positive");
        }
    }
}
