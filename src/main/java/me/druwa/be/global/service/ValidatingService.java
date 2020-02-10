package me.druwa.be.global.service;

import javax.validation.Validator;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidatingService {
    private final Validator validator;

    public <T> boolean isInvalid(final T object, Class<?> groups) {
        return validator.validate(object, groups).size() > 0;
    }
}
