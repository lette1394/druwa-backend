package me.druwa.be.domain.common.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import lombok.SneakyThrows;

public interface Mergeable<T> {

    @SneakyThrows
    default T merge(T other) {
        Field[] allFields = getClass().getDeclaredFields();
        for (Field field : allFields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (field.getDeclaredAnnotation(IgnoreMerge.class) != null) {
                continue;
            }

            if (!field.isAccessible() && Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
            }
            if (field.get(other) != null) {
                field.set(this, field.get(other));
            }
        }
        return (T) this;
    }
}
