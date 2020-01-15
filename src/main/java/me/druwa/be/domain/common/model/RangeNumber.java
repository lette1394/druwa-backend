package me.druwa.be.domain.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(of = "value")
public class RangeNumber<T extends Number> {
    private final T from;
    private final T to;

    @Getter
    @JsonProperty
    private final T value;

    @Builder
    public RangeNumber(final T from, final T to, final T value) {
        this.from = from;
        this.to = to;
        this.value = value;

        validate(from, value, to);
    }

    @JsonValue
    public String toString() {
        return String.valueOf(getValue());
    }

    private void validate(final T from, final T value, final T to) {
        if (value.doubleValue() < from.doubleValue() || value.doubleValue() > to.doubleValue()) {
            throw new IllegalArgumentException(value + " is not in range");
        }
    }
}
