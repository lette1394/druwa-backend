package me.druwa.be.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_EMAIL(10000),
    DUPLICATE_EMAIL(10001),
    INVALID_NAME(10002),
    INVALID_PASSWORD(10003),

    NO_ERROR_CODE(-1);

    @Getter
    private final long code;
}
