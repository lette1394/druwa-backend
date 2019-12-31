package me.druwa.be.docs;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.snippet.Attributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import static org.springframework.restdocs.snippet.Attributes.key;


@Slf4j
@RequiredArgsConstructor
public class ConstraintAttribute {
    private final Class<?> clazz;
    private static final String KEY = "constraints";

    public static ConstraintAttribute createAttribute(final Class<?> clazz) {
        return new ConstraintAttribute(clazz);
    }

    public Attributes.Attribute constraint(String property) {
        final ConstraintDescriptions userConstraints = new ConstraintDescriptions(clazz);
        final List<String> constraints = userConstraints.descriptionsForProperty(property);
        constraints.add(appendIfEnum(property));

        final String value = constraints.stream()
                                        .filter(StringUtils::isNotBlank)
                                        .map(constraint -> "- " + constraint)
                                        .collect(Collectors.joining("\n\n"));
        return key(KEY).value(value);
    }

    private String appendIfEnum(String property) {
        try {
            final Field field = clazz.getDeclaredField(property);
            field.setAccessible(true);
            if (Enum.class.isAssignableFrom(field.getType())) {
                final Object[] enumConstants = field.getType().getEnumConstants();
                return String.format("%s, %s, %s",
                                     "Must be one of ",
                                     Arrays.stream(enumConstants)
                                           .map(obj -> String.format("\"%s\"", obj))
                                           .collect(Collectors.joining(", ")),
                                     " (case sensitive)");
            }
        } catch (NoSuchFieldException e) {
            return StringUtils.EMPTY;
        }
        return StringUtils.EMPTY;
    }
}